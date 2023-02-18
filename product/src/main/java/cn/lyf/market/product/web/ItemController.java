package cn.lyf.market.product.web;

import cn.lyf.market.product.service.SkuInfoService;
import cn.lyf.market.product.vo.SkuInfoItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ItemController {
	@Autowired
	private SkuInfoService skuInfoService;

	@GetMapping("/{skuId}.html")
	public String skuItem(@PathVariable("skuId") Long skuId, Model model) {
		System.out.println("查询skuId:" + skuId);
		SkuInfoItemVo vo = skuInfoService.item(skuId);
		model.addAttribute("item", vo);
		return "item";
	}

}
