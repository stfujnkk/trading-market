package cn.lyf.market.product.service.impl;

import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.Query;
import cn.lyf.market.product.dao.AttrGroupDao;
import cn.lyf.market.product.entity.AttrGroupEntity;
import cn.lyf.market.product.service.AttrGroupService;
import cn.lyf.market.product.service.AttrService;
import cn.lyf.market.product.vo.GroupWithAttrVo;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        if (catelogId != null) wrapper.eq("catelog_id", catelogId);
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(obj -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public List<GroupWithAttrVo> queryGroupWithAttrs(Integer cateId) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id", cateId);
        List<AttrGroupEntity> attrGroupEntities = this.baseMapper.selectList(wrapper);
        List<GroupWithAttrVo> vos = attrGroupEntities.stream()
                .map(atg -> {
                    GroupWithAttrVo groupWithAttrVo = new GroupWithAttrVo();
                    BeanUtils.copyProperties(atg, groupWithAttrVo);
                    groupWithAttrVo.setAttrs(attrService.getRelationAttr(atg.getAttrGroupId()));
                    return groupWithAttrVo;
                }).collect(Collectors.toList());
        return vos;
    }

}
