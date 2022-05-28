package cn.lyf.market.order.dao;

import cn.lyf.market.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:20:52
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
