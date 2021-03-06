package cn.lyf.market.product.dao;

import cn.lyf.market.product.entity.AttrAttrgroupRelationEntity;
import cn.lyf.market.product.vo.AttrGroupRelationVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:18:19
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {
    Integer deleteRelations(@Param("list") List<AttrGroupRelationVo> relationVos);
}
