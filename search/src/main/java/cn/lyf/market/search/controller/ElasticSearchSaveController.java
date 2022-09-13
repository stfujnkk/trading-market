package cn.lyf.market.search.controller;

import cn.lyf.common.exception.BaseCodeEnum;
import cn.lyf.common.to.es.SkuEsModel;
import cn.lyf.common.utils.R;
import cn.lyf.market.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequestMapping("/search/save")
@RestController
public class ElasticSearchSaveController {

    @Autowired
    ProductSaveService productSaveService;

    /**
     * 上架商品
     */
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        try {
            if (productSaveService.productStatusUp(skuEsModels)) return R.ok();
        } catch (IOException e) {
            log.error("ElasticSearch 商品上架错误: {}", e);
        }
        return R.error(BaseCodeEnum.PRODUCT_UP_EXCEPTION);
    }
}
