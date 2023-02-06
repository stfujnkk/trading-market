package cn.lyf.market.search.vo;

import cn.lyf.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
	private List<SkuEsModel> products;
	private Integer pageNum;
	private Long total;
	private Integer totalPage;

	private List<BrandVo> brands;
	private List<AttrVo> attrs;
	private List<CatalogVo> catalogs;
	private List<Filter> filters;

	@Data
	public static class Filter {

		/**
		 * 参数子id
		 * <p>
		 * 如 attrs=3_安卓:鸿蒙 ,id应为3
		 */
		private Long id;

		/**
		 * 参数名字
		 * 如 attrs
		 */
		private String key;

		/**
		 * 前端显示的名称
		 * 如 手机系统
		 */
		private String name;

		private String value;

		/**
		 * 前端显示的值
		 * 如 安卓,鸿蒙
		 */
		private String showValue;

		/**
		 * 是否是列表，如果是聚合则表示value值为一个列表
		 */
		private Boolean isList;
	}

	@Data
	public static class BrandVo {
		private Long brandId;
		private String brandName;
		private String brandImg;
	}

	@Data
	public static class AttrVo {
		private Long attrId;
		private String attrName;
		private List<String> attrValue;
	}

	@Data
	public static class CatalogVo {
		private Long catalogId;
		private String catalogName;
	}
}
