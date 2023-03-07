package cn.lyf.market.member.exception;

public class PhoneExsitException extends RuntimeException{
	public PhoneExsitException(){
		super("手机号已经被注册");
	}
}
