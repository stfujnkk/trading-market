package cn.lyf.market.product.dao;

import cn.lyf.market.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:18:19
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
