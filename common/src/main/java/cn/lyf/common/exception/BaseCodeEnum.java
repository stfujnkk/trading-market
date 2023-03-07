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
	BUSINESS_ERROR(10003, "业务错误"),
	PRODUCT_UP_EXCEPTION(11001, "商品上架异常"),
	USER_EXIST_EXCEPTION(15001, "用户已经存在"),
	PHONE_EXIST_EXCEPTION(15002, "手机已经注册"),
	LOGIN_PASSWORD_INVALID_EXCEPTION(15003, "账号或密码错误");

	public final int code;
	public final String msg;

	BaseCodeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
