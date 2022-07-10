package cn.lyf.market.product.service.impl;

import cn.lyf.common.constant.ProductConstant;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.Query;
import cn.lyf.market.product.dao.AttrAttrgroupRelationDao;
import cn.lyf.market.product.dao.AttrDao;
import cn.lyf.market.product.dao.AttrGroupDao;
import cn.lyf.market.product.dao.CategoryDao;
import cn.lyf.market.product.entity.AttrAttrgroupRelationEntity;
import cn.lyf.market.product.entity.AttrEntity;
import cn.lyf.market.product.entity.AttrGroupEntity;
import cn.lyf.market.product.entity.CategoryEntity;
import cn.lyf.market.product.service.AttrService;
import cn.lyf.market.product.service.CategoryService;
import cn.lyf.market.product.vo.AttrGroupRelationVo;
import cn.lyf.market.product.vo.AttrRespVo;
import cn.lyf.market.product.vo.AttrVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    AttrAttrgroupRelationDao relationDao;
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    CategoryService categoryService;

    /**
     * 根据分组id查找关联的基本属性
     *
     * @param attrgroupId 属性分组id
     * @return 关联的属性列表
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> attrIds = relationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        return this.listByIds(attrIds);
    }

    @Override
    public void deleteRelationAttr(List<AttrGroupRelationVo> relationVos) {
        relationDao.deleteRelations(relationVos);
    }

    /**
     * 获取当前分组没有关联的所有属性
     * 当前分组只能关联自己所属分类里的且没被自己和别的分组引用的属性
     *
     * @param attrgroupId 组id
     * @param params      分页参数
     */
    @Override
    public PageUtils getNoRelationAttr(Long attrgroupId, Map<String, Object> params) {
        // 当前组id --> 分类id --> 同分类下所有组id -->排除 同分类所有组的已选的属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long categoryId = attrGroupEntity.getCatelogId();
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", categoryId));
        List<Long> gids = attrGroupEntities.stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", gids));
        List<Long> attrIds = attrAttrgroupRelationEntities.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id", categoryId)
                .eq("attr_type", ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())
                .notIn("attr_id", attrIds);
        //查询
        final String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(w -> w.eq("attr_id", key).or().like("attr_name", key));
        }
        // 分页
        return new PageUtils(this.page(new Query<AttrEntity>().getPage(params), wrapper));
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<>()
        );
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        // 保存基本数据
        this.save(attrEntity);
        if (attr.getAttrGroupId() == null) return;
        // 销售属性没有分组,只有分类
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) return;
        // 保存关联关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationDao.insert(relationEntity);
    }

    @Override
    public PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId) {
        return queryAttrPage(params, catelogId, "1");
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo respVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, respVo);
        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            // 设置分组信息
            AttrAttrgroupRelationEntity attrAttrgroupRelation = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
            if (attrAttrgroupRelation != null) {
                respVo.setAttrGroupId(attrAttrgroupRelation.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrId);
                respVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }
        // 设置分类信息
        Long catelogId = attrEntity.getCatelogId();
        List<Long> catelogPath = categoryService.findCatelogPath(catelogId);
        respVo.setCatelogPath(catelogPath);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity != null) {
            respVo.setCatelogName(categoryEntity.getName());
        }
        return respVo;
    }

    @Override
    @Transactional
    public void updateAttr(AttrVo attr) {
        this.updateById(attr);
        // 销售属性没有分组,只有分类
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) return;
        Integer count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());//AttrAttrgroupRelationEntity
        if (count > 0) {
            // 修改分组关联
            relationDao.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        } else {
            relationDao.insert(relationEntity);
        }
    }

    @Override
    public PageUtils querySaleAttrPage(Map<String, Object> params, Long catelogId) {
        return queryAttrPage(params, catelogId, "0");
    }

    private PageUtils queryAttrPage(Map<String, Object> params, Long catelogId, String attrType) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type", attrType);
        if (catelogId != 0) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and(w -> w.eq("attr_id", key).or().like("attr_name", key));
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        List<AttrEntity> records = page.getRecords();
        //XXX: 效率有问题？
        List<AttrRespVo> attrRespVos = records.stream().map(a -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(a, attrRespVo);
            // 销售属性没有分组,只有分类
            if (attrRespVo.getAttrType() != ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) {
                AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", a.getAttrId()));
                if (relationEntity != null && relationEntity.getAttrId() != null && relationEntity.getAttrGroupId() != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                    attrRespVo.setAttrGroupId(attrGroupEntity.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            CategoryEntity categoryEntity = categoryDao.selectById(a.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(attrRespVos);
        return pageUtils;
    }
}