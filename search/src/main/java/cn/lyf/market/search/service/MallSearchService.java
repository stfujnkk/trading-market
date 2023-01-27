package cn.lyf.market.search.service;

import cn.lyf.market.search.vo.SearchParam;
import cn.lyf.market.search.vo.SearchResponse;

public interface MallSearchService {
	SearchResponse search(SearchParam param);
}
