package cn.lyf.market.search.service.impl;

import cn.lyf.common.to.es.SkuEsModel;
import cn.lyf.market.search.config.ElasticSearchConfig;
import cn.lyf.market.search.constant.EsConstant;
import cn.lyf.market.search.service.MallSearchService;
import cn.lyf.market.search.vo.SearchParam;
import cn.lyf.market.search.vo.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MallSearchServiceImpl implements MallSearchService {

	@Autowired
	private RestHighLevelClient client;

	@Override
	public SearchResult search(SearchParam param) {
		// 构建es查询语句
		SearchRequest request = buildSearchRequest(param);
		SearchResult result = null;
		try {
			SearchResponse response = client.search(request, ElasticSearchConfig.COMMON_OPTIONS);
			// 封装数据为指定格式
			result = buildSearchResult(response);
			result.setPageNum(param.getPageNum());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param response
	 * @return
	 */
	private SearchResult buildSearchResult(SearchResponse response) throws JsonProcessingException {
		SearchResult result = new SearchResult();
		SearchHits hits = response.getHits();
		Aggregations aggregations = response.getAggregations();

		//region 商品
		List<SkuEsModel> skuEsModelList = new ArrayList<>();

		if (hits.getHits() != null && hits.getHits().length > 0) {
			for (SearchHit hit : hits.getHits()) {
				String sourceAsString = hit.getSourceAsString();
				ObjectMapper mapper = new ObjectMapper();
				SkuEsModel skuEsModel = mapper.readValue(sourceAsString, SkuEsModel.class);

				//region 设置高亮
				Map<String, HighlightField> highlightFields = hit.getHighlightFields();
				if (highlightFields != null && !highlightFields.isEmpty()) {
					HighlightField skuTitle = highlightFields.get("skuTitle");
					if (skuTitle != null) {
						String highlightSkuTitle = skuTitle.getFragments()[0].string();
						skuEsModel.setSkuTitle(highlightSkuTitle);
					}
				}
				//endregion 设置高亮

				skuEsModelList.add(skuEsModel);
			}
		}

		result.setProducts(skuEsModelList);
		//endregion 商品

		//region 属性
		List<SearchResult.AttrVo> attrVos = new ArrayList<>();
		ParsedNested attr_agg = aggregations.get("attr_agg");
		ParsedTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");

		for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
			SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
			// 属性id
			long attrId = bucket.getKeyAsNumber().longValue();
			attrVo.setAttrId(attrId);
			// 属性名称
			ParsedTerms attr_name_agg = bucket.getAggregations().get("attr_name_agg");
			String attrName = attr_name_agg.getBuckets().get(0).getKeyAsString();
			attrVo.setAttrName(attrName);
			// 属性值
			ParsedTerms attr_value_agg = bucket.getAggregations().get("attr_value_agg");
			List<String> attrValues = attr_value_agg.getBuckets().stream().map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());
			attrVo.setAttrValue(attrValues);

			attrVos.add(attrVo);
		}

		result.setAttrs(attrVos);
		//endregion 属性

		//region 品牌
		List<SearchResult.BrandVo> brandVos = new ArrayList<>();
		ParsedTerms brand_agg = aggregations.get("brand_agg");

		for (Terms.Bucket bucket : brand_agg.getBuckets()) {
			SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
			// 品牌id
			long brandId = bucket.getKeyAsNumber().longValue();
			brandVo.setBrandId(brandId);
			// 品牌名称
			ParsedTerms brand_name_agg = bucket.getAggregations().get("brand_name_agg");
			String brandName = brand_name_agg.getBuckets().get(0).getKeyAsString();
			brandVo.setBrandName(brandName);
			// 品牌图片
			ParsedTerms brand_img_agg = bucket.getAggregations().get("brand_img_agg");
			String brandImg = brand_img_agg.getBuckets().get(0).getKeyAsString();
			brandVo.setBrandImg(brandImg);

			brandVos.add(brandVo);
		}

		result.setBrands(brandVos);
		//endregion 品牌

		//region 分类
		ParsedTerms catalog_agg = aggregations.get("catalog_agg");
		List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
		List<SearchResult.CatalogVo> catalogs = new ArrayList();

		for (Terms.Bucket bucket : buckets) {
			SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
			// 分类id
			String catalogId = bucket.getKeyAsString();
			catalogVo.setCatalogId(Long.parseLong(catalogId));
			// 分类名称
			ParsedTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
			String catalogName = catalog_name_agg.getBuckets().get(0).getKeyAsString();
			catalogVo.setCatalogName(catalogName);

			catalogs.add(catalogVo);
		}

		result.setCatalogs(catalogs);
		//endregion 分类

		//region 分页信息

		Long total = hits.getTotalHits().value;
		result.setTotal(total);
		int totalPage = (int) ((total + EsConstant.PAGE_SIZE - 1) / EsConstant.PAGE_SIZE);
		result.setTotalPage(totalPage);

		//endregion 分页信息

		return result;
	}

	private SearchRequest buildSearchRequest(SearchParam param) {
		SearchSourceBuilder builder = new SearchSourceBuilder();
		//region 查询 query

		//region 布尔查询 bool

		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		builder.query(boolQuery);

		//region 模糊匹配 must
		if (!StringUtils.isEmpty(param.getKeyword())) {
			boolQuery.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
		}
		//endregion 模糊匹配 must

		//region 过滤 filter

		//region 按三级分类id过滤
		if (param.getCatalog3Id() != null) {
			boolQuery.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
		}
		//endregion 按三级分类id过滤

		//region 按品牌id列表过滤
		if (param.getBrandId() != null && param.getBrandId().size() > 0) {
			boolQuery.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
		}
		//endregion 按品牌id列表过滤

		//region 查询是否有库存
		if (param.getHasStock() != null) boolQuery.filter(QueryBuilders.termsQuery("hasStock", param.getHasStock()));
		//endregion 查询是否有库存

		//region 价格区间过滤
		if (!StringUtils.isEmpty(param.getSkuPrice())) {
			RangeQueryBuilder skuPriceRange = QueryBuilders.rangeQuery("skuPrice");
			final String skuPrice = param.getSkuPrice();
			if (skuPrice.startsWith("_")) {
				skuPriceRange.lte(skuPrice.substring(1));
			} else if (skuPrice.endsWith("_")) {
				skuPriceRange.gte(skuPrice.substring(0, skuPrice.length() - 1));
			} else {
				String[] prices = skuPrice.split("_");
				skuPriceRange.gte(prices[0]).lte(prices[1]);
			}
			boolQuery.filter(skuPriceRange);
		}
		//endregion 价格区间过滤

		//region 按照属性过滤
		if (param.getAttrs() != null && param.getAttrs().size() > 0) {
			for (String attr : param.getAttrs()) {
				BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
				// attrs=1_其他:安卓&attrs=2_10G
				String[] split1 = attr.split("_");
				final String attrId = split1[0];
				final String[] attrValues = split1[1].split(":");
				nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
				nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
				// 每个都生成一个nested查询
				NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
				boolQuery.filter(nestedQuery);
			}
		}
		//endregion 按照属性过滤

		//endregion 过滤 filter

		//endregion 布尔查询 bool

		//endregion 查询 query

		//region 排序 sort
		if (!StringUtils.isEmpty(param.getSort())) {
			String sort = param.getSort();
			String[] s = sort.split("_");
			SortOrder order = s[1].equalsIgnoreCase("ase") ? SortOrder.ASC : SortOrder.DESC;
			builder.sort(s[0], order);
		}
		//endregion 排序 sort

		//region 分页 from size
		builder.from(EsConstant.PAGE_SIZE * (param.getPageNum() - 1));
		builder.size(EsConstant.PAGE_SIZE);
		//endregion 分页 from size

		//region 高亮 highlight
		if (!StringUtils.isEmpty(param.getKeyword())) {
			HighlightBuilder highlightBuilder = new HighlightBuilder();
			highlightBuilder.field("skuTitle");
			highlightBuilder.preTags("<b style='color:red'>");
			highlightBuilder.postTags("</b>");
			builder.highlighter(highlightBuilder);
		}
		//endregion 高亮 highlight

		//region 聚合 aggs

		//region 品牌聚合 brand_agg
		TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
		brand_agg.field("brandId").size(50);
		builder.aggregation(brand_agg);

		//region brand_name_agg
		TermsAggregationBuilder brand_name_agg = AggregationBuilders.terms("brand_name_agg").field("brandName").size(1);
		brand_agg.subAggregation(brand_name_agg);
		//endregion brand_name_agg

		//region brand_img_agg
		TermsAggregationBuilder brand_img_agg = AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1);
		brand_agg.subAggregation(brand_img_agg);
		//endregion brand_img_agg

		//endregion 品牌聚合 brand_agg

		//region 分类聚合 catalog_agg
		TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg");
		catalog_agg.field("catalogId").size(50);
		builder.aggregation(catalog_agg);

		//region catalog_name_agg
		TermsAggregationBuilder catalog_name_agg = AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1);
		catalog_agg.subAggregation(catalog_name_agg);
		//endregion catalog_name_agg

		//endregion 分类聚合 catalog_agg

		//region 属性聚合 attr_agg
		final NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
		builder.aggregation(attr_agg);

		//region attr_id_agg

		TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
		attr_agg.subAggregation(attr_id_agg);

		//region attr_name_agg
		TermsAggregationBuilder attr_name_agg = AggregationBuilders.terms("attr_name_agg").field("attrs.attrName");
		attr_name_agg.size(1);
		attr_id_agg.subAggregation(attr_name_agg);
		//endregion attr_name_agg

		//region attr_value_agg
		TermsAggregationBuilder attr_value_agg = AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue");
		attr_value_agg.size(50);
		attr_id_agg.subAggregation(attr_value_agg);
		//endregion attr_value_agg

		//endregion attr_id_agg

		//endregion 属性聚合 attr_agg

		//endregion 聚合 aggs

		System.out.println("构建的DSL" + builder);
		SearchRequest searchResult = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, builder);
		return searchResult;
	}
}
