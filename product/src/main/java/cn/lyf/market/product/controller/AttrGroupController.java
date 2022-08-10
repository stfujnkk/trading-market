package cn.lyf.market.product.controller;

import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.R;
import cn.lyf.market.product.entity.AttrEntity;
import cn.lyf.market.product.entity.AttrGroupEntity;
import cn.lyf.market.product.service.AttrAttrgroupRelationService;
import cn.lyf.market.product.service.AttrGroupService;
import cn.lyf.market.product.service.AttrService;
import cn.lyf.market.product.service.CategoryService;
import cn.lyf.market.product.vo.AttrGroupRelationVo;
import cn.lyf.market.product.vo.GroupWithAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 属性分组
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 13:53:41
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AttrService attrService;

    @Autowired
    AttrAttrgroupRelationService relationService;

    @PostMapping("/attr/relation")
    public R addAttrGroupRelation(@RequestBody List<AttrGroupRelationVo> vos) {
        relationService.saveBatch(vos);
        return R.ok();
    }
//{"msg":"success","code":0,"data":[{"attrGroupId":1,"attrGroupName":"主体","sort":0,"descript":"主体","icon":"dd","catelogId":225,"attrs":[{"attrId":7,"attrName":"入网型号","searchType":1,"valueType":0,"icon":"xxx","valueSelect":"A2217;C3J;以官网信息为准","attrType":1,"enable":1,"catelogId":225,"showDesc":0},{"attrId":8,"attrName":"上市年份","searchType":0,"valueType":0,"icon":"xxx","valueSelect":"2018;2019","attrType":1,"enable":1,"catelogId":225,"showDesc":0},{"attrId":21,"attrName":"test","searchType":0,"valueType":0,"icon":"yyy","valueSelect":"","attrType":1,"enable":1,"catelogId":225,"showDesc":0}]},{"attrGroupId":2,"attrGroupName":"基本信息","sort":0,"descript":"基本信息","icon":"xx","catelogId":225,"attrs":[{"attrId":11,"attrName":"机身颜色","searchType":0,"valueType":0,"icon":"xxx","valueSelect":"黑色;白色","attrType":1,"enable":1,"catelogId":225,"showDesc":1},{"attrId":13,"attrName":"机身长度（mm）","searchType":0,"valueType":0,"icon":"xxxxx","valueSelect":"158.3;135.9","attrType":1,"enable":1,"catelogId":225,"showDesc":0},{"attrId":14,"attrName":"机身材质工艺","searchType":0,"valueType":1,"icon":"xxx","valueSelect":"以官网信息为准;陶瓷;玻璃","attrType":1,"enable":1,"catelogId":225,"showDesc":0},{"attrId":22,"attrName":"test1","searchType":0,"valueType":1,"icon":"xxyyy","valueSelect":"","attrType":1,"enable":1,"catelogId":225,"showDesc":0}]},{"attrGroupId":7,"attrGroupName":"主芯片","sort":0,"descript":"主芯片","icon":"xx","catelogId":225,"attrs":[{"attrId":15,"attrName":"CPU品牌","searchType":1,"valueType":0,"icon":"xxx","valueSelect":"高通(Qualcomm);海思（Hisilicon）;以官网信息为准","attrType":1,"enable":1,"catelogId":225,"showDesc":1},{"attrId":16,"attrName":"CPU型号","searchType":1,"valueType":0,"icon":"xxx","valueSelect":"骁龙665;骁龙845;骁龙855;骁龙730;HUAWEI Kirin 980;HUAWEI Kirin 970","attrType":1,"enable":1,"catelogId":225,"showDesc":0}]},{"attrGroupId":8,"attrGroupName":"屏幕","sort":0,"descript":"屏幕","icon":"xx","catelogId":225,"attrs":[]}]}
    ///product/attrgroup/0/withattr
    @GetMapping("/{cateId}/withattr")
    public R queryWithAttr(@PathVariable(name = "cateId") Integer cateId) {
        List<GroupWithAttrVo> vos= attrGroupService.queryGroupWithAttrs(cateId);
        return R.ok().put("data",vos);
    }


    /**
     * 列表 无id时获取所有,有id时获取对应分类
     */
    @RequestMapping(value = {"/list/{id}", "/list"})
    public R list(@RequestParam Map<String, Object> params, @PathVariable(name = "id", required = false) Long catelogId) {
        PageUtils page = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        List<Long> path = categoryService.findCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));
        return R.ok();
    }

    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId) {
        List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data", entities);
    }

    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody List<AttrGroupRelationVo> relationVos) {
        attrService.deleteRelationAttr(relationVos);
        return R.ok();
    }

    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrgroupId") Long attrgroupId, @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getNoRelationAttr(attrgroupId, params);
        return R.ok().put("page", page);
    }
}
