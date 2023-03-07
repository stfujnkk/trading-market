package cn.lyf.market.auth.feign;

import cn.lyf.common.utils.R;
import cn.lyf.market.auth.vo.UserLoinVo;
import cn.lyf.market.auth.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("member")
public interface MemberFeignService {

	@PostMapping("/member/member/register")
	R register(@RequestBody UserRegisterVo vo);

	@PostMapping("/member/member/login")
	R login(@RequestBody UserLoinVo vo);

}
