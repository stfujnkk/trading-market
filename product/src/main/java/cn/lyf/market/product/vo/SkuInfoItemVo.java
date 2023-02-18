package cn.lyf.market.product.vo;

import cn.lyf.market.product.entity.SkuImagesEntity;
import cn.lyf.market.product.entity.SkuInfoEntity;
import cn.lyf.market.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

@Data
public class SkuInfoItemVo {
	// sku基本信息 标题, pms_sku_info
	private SkuInfoEntity info;

	// sku 图片 pms_sku_images
	private List<SkuImagesEntity> images;

	// 获取spu销售属性组合
	private List<SpuAttrGroupVo> groupAttrs;

	// spu商品介绍
	private SpuInfoDescEntity descp;

	// spu规格参数信息
	private List<ItemSaleAttrVo> saleAttr;

	private boolean hasStock = true;

}
