package cn.lyf.market.product.controller;

import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.R;
import cn.lyf.market.product.entity.CategoryBrandRelationEntity;
import cn.lyf.market.product.service.CategoryBrandRelationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 品牌分类关联
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 13:53:41
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 列表
     */
    @GetMapping("/catelog/list")
    public R catelogList(@RequestParam("brandId") Long brandId) {
        QueryWrapper queryWrapper = new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId);
        List<CategoryBrandRelationEntity> data = categoryBrandRelationService.list(queryWrapper);
        return R.ok().put("data", data);
    }

    /**
     * 查出该分类的所有品牌
     *
     * @param catId 分类id
     * @return
     */
    @GetMapping("/brands/list")
    public R listBrands(@RequestParam("catId") Integer catId) {
        /* SELECT brand_id,brand_name FROM pms_category_brand_relation
         GROUP BY brand_id HAVING catelog_id = ? */
        if (catId < 0) return R.error("catId为负数");
        QueryWrapper<CategoryBrandRelationEntity> condition = new QueryWrapper<>();
        if (catId > 0) {
            // 查询特定分类的品牌
            condition.eq("catelog_id", catId);
        }
        condition.groupBy("brand_id");
        return R.ok().put("page", categoryBrandRelationService.listMaps(condition));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.saveDetail(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
