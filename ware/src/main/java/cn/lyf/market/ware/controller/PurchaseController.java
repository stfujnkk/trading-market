package cn.lyf.market.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.lyf.market.ware.vo.MergeVo;
import cn.lyf.market.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.lyf.market.ware.entity.PurchaseEntity;
import cn.lyf.market.ware.service.PurchaseService;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.R;


/**
 * 采购信息
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 13:51:12
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 查询未领取的采购单
     */
    @RequestMapping("/unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPageUnreceivePurchase(params);

        return R.ok().put("page", page);
    }

    /**
     * 合并到采购单,若无未选择采购单则新增一个
     */
    @PostMapping("/merge")
    public R mergePurchase(@RequestBody MergeVo mergeVo) {
        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }

    /**
     * 外部接口
     * 领取采购单
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids) {
        // TODO 只能领取自己的未领取采购单
        purchaseService.received(ids);
        return R.ok();
    }

    /**
     * 外部接口
     * 完成采购单
     */
    @PostMapping("/done")
    public R done(@RequestBody PurchaseDoneVo doneVo) {
        // { id, items: [{itemId,status,reason}] }
        purchaseService.done(doneVo);
        return R.ok();
    }

}
