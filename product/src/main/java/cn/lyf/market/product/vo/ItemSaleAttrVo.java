package cn.lyf.market.product.vo;

import lombok.Data;

import java.util.List;

@Data
public class ItemSaleAttrVo {
	private Long attrId;
	private String attrName;
	private List<AttrValue> attrValues;

	@Data
	public static class AttrValue {
		private String attrValue;
		private String skuIds;
	}
}
