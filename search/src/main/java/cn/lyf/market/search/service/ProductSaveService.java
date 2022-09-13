package cn.lyf.market.search.service;

import cn.lyf.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

public interface ProductSaveService {
    /**
     * 上架商品,成功返回true
     */
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
