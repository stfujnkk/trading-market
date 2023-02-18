package cn.lyf.market.product.service;

import cn.lyf.market.product.vo.ItemSaleAttrVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.market.product.entity.SkuSaleAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:18:19
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

	List<ItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId);
}

