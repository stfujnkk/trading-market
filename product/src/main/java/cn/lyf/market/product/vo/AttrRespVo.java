package cn.lyf.market.product.vo;

import cn.lyf.market.product.entity.AttrEntity;
import lombok.Data;

@Data
public class AttrRespVo extends AttrEntity {
    private String catelogName;
    private String groupName;
    private Long attrGroupId;
}
