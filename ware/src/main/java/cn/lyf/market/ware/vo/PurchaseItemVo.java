package cn.lyf.market.ware.vo;

import lombok.Data;

/**
 * 采购项
 */
@Data
public class PurchaseItemVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
