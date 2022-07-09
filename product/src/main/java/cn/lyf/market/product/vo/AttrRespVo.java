package cn.lyf.market.product.vo;

import cn.lyf.market.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrRespVo extends AttrEntity {
    private String catelogName;
    private String groupName;
    private Long attrGroupId;
    private List<Long> catelogPath;
}
