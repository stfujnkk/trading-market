package cn.lyf.common.exception;

/**
 * 10: 通用
 * 11: 商品
 * 12: 订单
 * 13: 购物车
 * 14: 物流
 */
public enum BaseCodeEnum {
    UNKNOWN_EXCEPTION(10000, "未知错误"),
    VALID_EXCEPTION(10001, "数据校验错误"),
    OP_EXCEPTION(10002, "操作类错误"),

    PRODUCT_UP_EXCEPTION(11000,"商品上架异常");

    public final int code;
    public final String msg;

    BaseCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
