package cn.lyf.market.ware.service;

import cn.lyf.market.ware.vo.SkuHasStockVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.market.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:16:11
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 商品入库
     * @param skuId sku的id
     * @param wareId 仓库id
     * @param skuNum 数量
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds);
}

