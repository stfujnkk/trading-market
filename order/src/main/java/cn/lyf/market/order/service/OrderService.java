package cn.lyf.market.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.market.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:20:52
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

