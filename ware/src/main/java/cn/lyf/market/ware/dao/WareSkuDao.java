package cn.lyf.market.ware.dao;

import cn.lyf.market.ware.entity.WareSkuEntity;
import cn.lyf.market.ware.vo.SkuHasStockVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:16:11
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    List<SkuHasStockVo> getSkusHasStock(@Param("skuIds")  List<Long> skuIds);
}
