package cn.lyf.market.product.service.impl;

import cn.lyf.market.product.entity.SkuImagesEntity;
import cn.lyf.market.product.entity.SpuInfoDescEntity;
import cn.lyf.market.product.service.*;
import cn.lyf.market.product.vo.ItemSaleAttrVo;
import cn.lyf.market.product.vo.SkuInfoItemVo;
import cn.lyf.market.product.vo.SpuAttrGroupVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.Query;

import cn.lyf.market.product.dao.SkuInfoDao;
import cn.lyf.market.product.entity.SkuInfoEntity;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

	@Autowired
	SkuImagesService skuImagesService;

	@Autowired
	SpuInfoDescService spuInfoDescService;

	@Autowired
	AttrGroupService attrGroupService;

	@Autowired
	SkuSaleAttrValueService skuSaleAttrValueService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<SkuInfoEntity> page = this.page(
				new Query<SkuInfoEntity>().getPage(params),
				new QueryWrapper<SkuInfoEntity>()
		);

		return new PageUtils(page);
	}

	@Override
	public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
		this.baseMapper.insert(skuInfoEntity);
	}

	@Override
	public PageUtils queryPageByCondition(Map<String, Object> params) {
		QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
		String key = (String) params.get("key");
		if (!StringUtils.isEmpty(key)) {
			queryWrapper.and(w -> {
				w.eq("sku_id", key).or().like("sku_name", key);
			});
		}

		String brandId = (String) params.get("brandId");
		if (!StringUtils.isEmpty(brandId) && !"0".equals(brandId)) {
			queryWrapper.eq("brand_id", brandId);
		}
		String catelogId = (String) params.get("catelogId");
		if (!StringUtils.isEmpty(catelogId) && !"0".equals(catelogId)) {
			queryWrapper.eq("catalog_id", catelogId);
		}
		String min = (String) params.get("min");
		if (!StringUtils.isEmpty(min)) {
			queryWrapper.ge("price", min);
		}
		String max = (String) params.get("max");
		if (!StringUtils.isEmpty(min) && !"0".equals(max)) {
			queryWrapper.le("price", max);
		}
		IPage<SkuInfoEntity> page = this.page(
				new Query<SkuInfoEntity>().getPage(params),
				queryWrapper
		);
		return new PageUtils(page);
	}

	@Override
	public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
		List<SkuInfoEntity> spuInfos = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
		return spuInfos;
	}

	@Override
	public SkuInfoItemVo item(Long skuId) {
		SkuInfoItemVo itemVo = new SkuInfoItemVo();
		// sku基本信息 标题, pms_sku_info
		SkuInfoEntity info = getById(skuId);
		itemVo.setInfo(info);
		Long spuId = info.getSpuId();
		Long catalogId = info.getCatalogId();
		// sku 图片 pms_sku_images
		List<SkuImagesEntity> images = skuImagesService.getImagesBySkuId(skuId);
		itemVo.setImages(images);
		// 获取spu销售属性组合
		List<ItemSaleAttrVo> saleAttrVo = skuSaleAttrValueService.getSaleAttrsBySpuId(spuId);
		itemVo.setSaleAttr(saleAttrVo);
		// spu商品介绍
		SpuInfoDescEntity spuInfoDescEntity = spuInfoDescService.getById(spuId);
		itemVo.setDescp(spuInfoDescEntity);
		// spu规格参数信息
		List<SpuAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
		itemVo.setGroupAttrs(attrGroupVos);
		return itemVo;
	}

}
