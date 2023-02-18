package cn.lyf.market.product.dao;

import cn.lyf.market.product.entity.AttrGroupEntity;
import cn.lyf.market.product.vo.SpuAttrGroupVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:18:19
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {

	List<SpuAttrGroupVo> getAttrGroupWithAttrsBySpuId(@Param("spuId") Long spuId, @Param("catalogId") Long catalogId);
}
