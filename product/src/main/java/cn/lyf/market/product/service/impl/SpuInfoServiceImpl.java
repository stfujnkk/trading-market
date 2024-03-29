package cn.lyf.market.product.service.impl;

import cn.lyf.common.constant.ProductConstant;
import cn.lyf.common.to.SkuReductionTo;
import cn.lyf.common.to.SpuBoundTo;
import cn.lyf.common.to.es.SkuEsModel;
import cn.lyf.common.utils.R;
import cn.lyf.market.product.entity.*;
import cn.lyf.market.product.feign.CouponFeignService;
import cn.lyf.market.product.feign.SearchFeignService;
import cn.lyf.market.product.feign.WareFeignService;
import cn.lyf.market.product.service.*;
import cn.lyf.market.product.vo.*;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.Query;

import cn.lyf.market.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService attrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * TODO 失败的处理逻辑
     *
     * @param vo
     */
    @Override
    @Transactional
    public void saveSpuInfo(SpuSaveVo vo) {
        // 保存 spu 基础属性 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);
        // 保存 spu 描述图片 pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", decript));
        spuInfoDescService.saveSpuInfoDesc(spuInfoDescEntity);
        // 保存 spu 图片集 pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);
        // 保存 spu 规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            AttrEntity a = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(a.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(spuInfoEntity.getId());
            return valueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveProductAttr(collect);
        // 保存spu积分信息 sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        R r = couponFeignService.saveSpuBounds(spuBoundTo);
        if (r.getCode() != 0) {
            log.error("远程保存spu积分信息失败:" + r.getMsg());
        }
        // 保存 spu 的 sku 信息
        List<Skus> skus = vo.getSkus();
        if (skus != null) {
            skus.forEach(sku -> {
                String defaultImgUrl = "";
                for (Images image : sku.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImgUrl = image.getImgUrl();
                        break;
                    }
                }
                // sku 基本信息 pms_sku_info
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImgUrl);
                skuInfoService.saveSkuInfo(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();
                // sku 图片信息 pms_sku_images
                List<SkuImagesEntity> skuImagesEntities = sku.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);// TODO 没有url路径的不报错
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    return skuImagesEntity;
                }).filter(entity -> !StringUtils.isEmpty(entity.getImgUrl())).collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntities);
                // sku 销售属性信息 pms_sku_sale_attr_value
                List<Attr> attrList = sku.getAttr();
                List<SkuSaleAttrValueEntity> saleAttrValueEntities = attrList.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);
                    return attrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(saleAttrValueEntities);
                // sku 优惠、满减信息 sms_sku_ladder, sms_sku_full_reduction,sms_member_price,
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(sku, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(BigDecimal.ZERO) > 0) {
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if (r1.getCode() != 0) {
                        log.error("远程保存sku优惠信息失败:" + r1.getMsg());
                    }
                }
            });
        }
    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and(w -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("publish_status", status);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equals(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equals(catelogId)) {
            queryWrapper.eq("catelog_id", catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        // 1) 组装需要的数据
        List<SkuInfoEntity> skuInfoEntities = skuInfoService.getSkusBySpuId(spuId);
        if (skuInfoEntities.size() == 0) {
            log.error("spuId为{}的商品没有对应的sku", spuId);
            throw new RuntimeException("商品没有对应的sku");
        }
        // 查询所有可以被检索的规格属性
        List<ProductAttrValueEntity> baseAttrs = attrValueService.baseAttrListForSpu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> attr.getAttrId()).collect(Collectors.toList());
        List<Long> searchAttrIds = attrService.selectAttrIds(attrIds);
        Set<Long> idSet = new HashSet<>(searchAttrIds);
        final List<SkuEsModel.Attrs> attrsList = baseAttrs.stream()
                .filter(attr -> idSet.contains(attr.getAttrId()))
                .map(attr -> {
                    SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
                    BeanUtils.copyProperties(attr, attrs);
                    return attrs;
                })
                .collect(Collectors.toList());
        // 发送远程调用，判断是否有库存
        List<Long> skuIds = skuInfoEntities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        Map<Long, Boolean> stockMap = null;
        try {
            R skusHasStock = wareFeignService.getSkusHasStock(skuIds);
            stockMap = skusHasStock.getData(new TypeReference<List<SkuHasStockVo>>() {
            }).stream().collect(Collectors.toMap(
                    SkuHasStockVo::getSkuId,
                    SkuHasStockVo::getHasStock
            ));
        } catch (Exception e) {
            log.error("库存服务查询异常 {}", e);
        }

        final Map<Long, Boolean> finalStockMap = stockMap;
        // 2) 封装sku信息
        List<SkuEsModel> upProducts = skuInfoEntities.stream().map(skuInfo -> {
            SkuEsModel esModel = new SkuEsModel();
            BeanUtils.copyProperties(skuInfo, esModel);
            esModel.setSkuPrice(skuInfo.getPrice());
            esModel.setSkuImg(skuInfo.getSkuDefaultImg());
            // TODO 热度评分
            esModel.setHotScore(0L);
            // 品牌和分类名字信息
            BrandEntity brandEntity = brandService.getById(esModel.getBrandId());
            esModel.setBrandName(brandEntity.getName());
            esModel.setBrandImg(brandEntity.getLogo());
            // 设置是否有库存
            if (finalStockMap == null || finalStockMap.get(skuInfo.getSkuId()) == null) {
                esModel.setHasStock(false);
            } else {
                esModel.setHasStock(finalStockMap.get(skuInfo.getSkuId()));
            }
            CategoryEntity categoryEntity = categoryService.getById(esModel.getCatalogId());
            esModel.setCatalogName(categoryEntity.getName());
            // 设置检索属性
            esModel.setAttrs(attrsList);
            log.debug(esModel.toString());
            return esModel;
        }).collect(Collectors.toList());

        R r = searchFeignService.productStatusUp(upProducts);
        if (0 == r.getCode()) {
            // 发布成功,修改spu状态
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        } else {
            // TODO　重复调用？接口幂等性。重试机制
        }
    }

}
