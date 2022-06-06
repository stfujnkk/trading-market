package cn.lyf.market.product.service.impl;

import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.Query;
import cn.lyf.market.product.dao.CategoryDao;
import cn.lyf.market.product.entity.CategoryEntity;
import cn.lyf.market.product.service.CategoryBrandRelationService;
import cn.lyf.market.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //TMP baseMapper.selectList(null);
        List<CategoryEntity> categoryList = baseMapper.selectList(null);
        Map<Long, List<CategoryEntity>> menus =
                categoryList.stream().collect(Collectors.groupingBy(CategoryEntity::getParentCid));
        Comparator<CategoryEntity> cmp = (a, b) -> {
            Integer sa = a.getSort(), sb = b.getSort();
            return (sa == null ? 0 : sa) - (sb == null ? 0 : sb);
        };
        categoryList.stream().forEach(x -> {
            Long id = x.getCatId();
            List<CategoryEntity> children = menus.get(id);
            if (children == null) return;
            children.sort(cmp);
            x.setChildren(children);
        });
        return categoryList.stream().filter(x -> x.getParentCid() == 0).sorted(cmp).collect(Collectors.toList());
    }

    @Override
    public void removeMenuByIds(List<Long> ids) {
        // TODO 检查菜单是否被引用
        baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<Long> findCatelogPath(Long catelogId) {
        List<Long> ans = new ArrayList<>();
        findParentCatelogId(ans, catelogId);
        return ans;
    }

    @Override
    //级联更新关系表
    public void updateCascade(CategoryEntity category) {
        baseMapper.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    private void findParentCatelogId(List<Long> ans, Long catelogId) {
        if (catelogId == null || catelogId == 0) return;
        CategoryEntity now = this.getById(catelogId);
        findParentCatelogId(ans, now.getParentCid());
        ans.add(catelogId);
    }

}