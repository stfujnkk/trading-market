package cn.lyf.market.auth.controller;

import cn.lyf.common.utils.R;
import cn.lyf.market.auth.feign.MemberFeignService;
import cn.lyf.market.auth.vo.UserLoinVo;
import cn.lyf.market.auth.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class LoginController {

	@Autowired
	MemberFeignService memberFeignService;


	@PostMapping("/register")
	public String register(@Valid UserRegisterVo vo, BindingResult result, RedirectAttributes redirectAttributes) {
		// System.out.println(vo);
		if (result.hasErrors()) {
			BiConsumer<List<String>, FieldError> accumulator = (arr, item) -> arr.add(item.getDefaultMessage());
			Supplier<List<String>> supplier = ArrayList::new;
			BinaryOperator<List<String>> combiner = (left, right) -> {
				left.addAll(right);
				return left;
			};
			Collector<FieldError, List<String>, List<String>> myCollector = Collector.of(supplier, accumulator, combiner);
			Map<String, List<String>> tmpErrors = result.getFieldErrors().stream().collect(Collectors.groupingBy(FieldError::getField, myCollector));
			// model.addAttribute("errors", errors);
			// return "forward:/reg.html";
			Map<String, String> errors = new HashMap<>();
			for (Map.Entry<String, List<String>> kv : tmpErrors.entrySet()) {
				errors.put(kv.getKey(), kv.getValue().stream().collect(Collectors.joining(",")));
			}
			redirectAttributes.addFlashAttribute("errors", errors);
			// TODO 重定向携带数据，利用session。将数据存在session中。
			// TODO 分布式下session问题
			return "redirect:http://auth.jmall.com/reg.html";
		}
		// 注册
		if (!checkVerificationCode(vo.getCode())) {
			Map<String, String> errors = new HashMap<>();
			errors.put("code", "验证码错误");
			redirectAttributes.addFlashAttribute("errors", errors);
			return "redirect:http://auth.jmall.com/reg.html";
		}
		R r = memberFeignService.register(vo);
		if (r.getCode() != 0) {
			String msg = r.getMsg();
			Map<String, String> errors = new HashMap<>();
			errors.put("msg", msg);
			redirectAttributes.addFlashAttribute("errors", errors);
			return "redirect:http://auth.jmall.com/reg.html";
		}
		return "redirect:http://auth.jmall.com/login.html";
	}

	/**
	 * 检验验证码
	 *
	 * @param code 验证码
	 * @return
	 */
	boolean checkVerificationCode(String code) {
		// TODO 验证码服务 213
		try {
			long l = Long.parseLong(code);
			return l < 5000L;
		} catch (Exception e) {
			return false;
		}
	}


	@PostMapping("/login")
	public String login(UserLoinVo vo, RedirectAttributes redirectAttributes) {
		R r = memberFeignService.login(vo);
		if (r.getCode() != 0) {
			Map<String, String> errors = new HashMap<>();
			errors.put("msg", r.getMsg());
			redirectAttributes.addFlashAttribute("errors", errors);
			return "redirect:http://auth.jmall.com/login.html";
		}
		return "redirect:http://jmall.com";
	}


}








