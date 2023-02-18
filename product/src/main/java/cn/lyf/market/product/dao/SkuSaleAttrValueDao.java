package cn.lyf.market.product.dao;

import cn.lyf.market.product.entity.SkuSaleAttrValueEntity;
import cn.lyf.market.product.vo.ItemSaleAttrVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * sku销售属性&值
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:18:19
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

	List<ItemSaleAttrVo> getSaleAttrsBySpuId(@Param("spuId") Long spuId);
}
