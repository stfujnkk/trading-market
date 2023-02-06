package cn.lyf.market.search.feign;

import cn.lyf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("product")
public interface ProductFeignService {

	/**
	 * 获取属性信息
	 */
	@RequestMapping("/product/attr/info/{attrId}")
	R attrInfo(@PathVariable("attrId") Long attrId);

	@GetMapping("/product/brand/infos")
	R brandInfo(@RequestParam("brandIds") List<Long> brandIds);

	/**
	 * 信息
	 */
	@RequestMapping("/product/category/info/{catId}")
	R catalogInfo(@PathVariable("catId") Long catId);
}
