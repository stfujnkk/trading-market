package cn.lyf.market.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.market.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:18:19
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImages(Long id, List<String> images);
}

