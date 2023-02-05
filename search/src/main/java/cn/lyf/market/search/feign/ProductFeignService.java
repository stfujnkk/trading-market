package cn.lyf.market.search.feign;

import cn.lyf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("product")
public interface ProductFeignService {

	/**
	 * 获取属性信息
	 */
	@RequestMapping("/product/attr/info/{attrId}")
	R attrInfo(@PathVariable("attrId") Long attrId);
}
