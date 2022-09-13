package cn.lyf.market.product.dao;

import cn.lyf.market.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:18:19
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> selectAttrIds(@Param("attrIds") List<Long> attrIds);
}
