package cn.lyf.market.coupon.feign;

import cn.lyf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("member")
@RequestMapping("member/growthchangehistory")
public interface MemberFeignService {
    @RequestMapping("/list")// TEST
    R growthchangehistoryList();
}
