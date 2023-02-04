package cn.lyf.market.search.service;

import cn.lyf.market.search.vo.SearchParam;
import cn.lyf.market.search.vo.SearchResult;

public interface MallSearchService {
	SearchResult search(SearchParam param);
}
