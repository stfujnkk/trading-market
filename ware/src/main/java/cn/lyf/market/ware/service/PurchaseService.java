package cn.lyf.market.ware.service;

import cn.lyf.market.ware.vo.MergeVo;
import cn.lyf.market.ware.vo.PurchaseDoneVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.market.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:16:11
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo doneVo);
}

