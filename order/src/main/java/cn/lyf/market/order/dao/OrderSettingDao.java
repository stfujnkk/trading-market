package cn.lyf.market.order.dao;

import cn.lyf.market.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:20:53
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}
