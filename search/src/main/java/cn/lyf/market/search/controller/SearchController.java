package cn.lyf.market.search.controller;

import cn.lyf.market.search.service.MallSearchService;
import cn.lyf.market.search.vo.SearchParam;
import cn.lyf.market.search.vo.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController {

	@Autowired
	MallSearchService mallSearchService;

	@GetMapping("list.html")
	public String listPage(SearchParam param, Model model) {
		SearchResponse result = mallSearchService.search(param);
		model.addAttribute("result", result);
		return "list";
	}
}
