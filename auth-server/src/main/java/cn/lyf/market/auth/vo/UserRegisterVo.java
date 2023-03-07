package cn.lyf.market.auth.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UserRegisterVo {
	@NotEmpty(message = "用户名必须提交")
	@Length(min=4,max = 20,message = "用户名必须是6-18位字符")
	private String userName;

	@NotEmpty(message = "密码不能为空")
	@Length(min=6,max = 20,message = "密码必须是6-18位字符")
	private String passWord;

	@Pattern(regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",message = "手机号格式不正确")
	@NotEmpty(message = "手机号必须填写")
	private String phone;

	@NotEmpty(message = "验证码必须填写")
	private String code;
}
