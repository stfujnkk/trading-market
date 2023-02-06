package cn.lyf.market.product.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {

	@GetMapping("/{skuId}.html")
	public String skuItem(Long skuId) {
		System.out.println("查询skuId:" + skuId);
		return "item";
	}

}
