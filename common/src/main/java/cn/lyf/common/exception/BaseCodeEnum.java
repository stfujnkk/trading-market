package cn.lyf.common.exception;

public enum BaseCodeEnum {
    UNKNOWN_EXCEPTION(10000, "未知错误"),
    VALID_EXCEPTION(10001, "数据校验错误"),
    OP_EXCEPTION(10002, "操作类错误");

    public final int code;
    public final String msg;

    BaseCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
