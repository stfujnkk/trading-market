package cn.lyf.market.product.vo;

import lombok.Data;

import java.util.List;

@Data
public class SpuAttrGroupVo {
	private String groupName;
	private List<Attr> attrs;
}
