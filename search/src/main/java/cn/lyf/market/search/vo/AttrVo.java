package cn.lyf.market.search.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttrVo {
	private Long attrId;
	private String attrName;
}
