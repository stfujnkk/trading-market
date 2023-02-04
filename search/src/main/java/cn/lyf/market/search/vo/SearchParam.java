package cn.lyf.market.search.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchParam {
	private String keyword;
	private Long catalog3Id;
	/**
	 * 排序条件
	 * sort=saleCount_asc/desc
	 * sort=skuPrice_asc/desc
	 * sort=hotScore_asc/desc
	 */
	private String sort;

	private Boolean hasStock = true;
	/**
	 * skuPrice=100_200/_200/100_
	 */
	private String skuPrice;
	private List<Long> brandId;
	/**
	 * attrs=1_其他:安卓&attrs=2_10G
	 */
	private List<String> attrs;
	private Integer pageNum = 1;
}
