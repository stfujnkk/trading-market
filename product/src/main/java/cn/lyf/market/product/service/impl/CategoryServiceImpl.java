package cn.lyf.market.product.service.impl;

import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.Query;
import cn.lyf.market.product.dao.CategoryDao;
import cn.lyf.market.product.entity.CategoryEntity;
import cn.lyf.market.product.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

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
        categoryList.stream().forEach(x -> {
            Long id = x.getCatId();
            x.setChildren(menus.get(id));
        });
        return categoryList.stream().filter(x -> x.getParentCid() == 0).collect(Collectors.toList());
    }

}