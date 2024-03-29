package cn.lyf.market.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.Query;

import cn.lyf.market.product.dao.SkuImagesDao;
import cn.lyf.market.product.entity.SkuImagesEntity;
import cn.lyf.market.product.service.SkuImagesService;


@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<SkuImagesEntity> page = this.page(
				new Query<SkuImagesEntity>().getPage(params),
				new QueryWrapper<SkuImagesEntity>()
		);

		return new PageUtils(page);
	}

	@Override
	public List<SkuImagesEntity> getImagesBySkuId(Long skuId) {
		SkuImagesDao imagesDao = this.baseMapper;
		List<SkuImagesEntity> imagesEntities = imagesDao.selectList(new QueryWrapper<SkuImagesEntity>().eq("sku_id", skuId));
		return imagesEntities;
	}

}















