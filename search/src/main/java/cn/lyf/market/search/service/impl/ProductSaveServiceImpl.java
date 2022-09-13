package cn.lyf.market.search.service.impl;

import cn.lyf.common.to.es.SkuEsModel;
import cn.lyf.common.utils.JsonUtils;
import cn.lyf.market.search.config.ElasticSearchConfig;
import cn.lyf.market.search.constant.EsConstant;
import cn.lyf.market.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient esClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        if (skuEsModels.size() == 0) return true;
        // 1、给es建立索引
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String jsonString = JsonUtils.beanToJsonString(skuEsModel);
            indexRequest.source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = esClient.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
        if (bulk.hasFailures()) {
            List<Long> collect = skuEsModels.stream().map(SkuEsModel::getSkuId).collect(Collectors.toList());
            log.error("商品上架错误 【{}】", collect);
            return false;
        }
        return true;
    }
}
