package cn.lyf.market.coupon.dao;

import cn.lyf.market.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:11:27
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
