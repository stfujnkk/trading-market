package cn.lyf.market.product.service;

import cn.lyf.market.product.entity.SpuInfoDescEntity;
import cn.lyf.market.product.vo.SpuSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.market.product.entity.SpuInfoEntity;

import java.util.Map;

/**
 * spu信息
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:18:19
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity);

}

