package cn.lyf.market.member.controller;

import java.util.Arrays;
import java.util.Map;

import cn.lyf.common.exception.BaseCodeEnum;
import cn.lyf.market.member.exception.PhoneExsitException;
import cn.lyf.market.member.exception.UserNameExistException;
import cn.lyf.market.member.vo.MemberLoginVo;
import cn.lyf.market.member.vo.MemberRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import cn.lyf.market.member.entity.MemberEntity;
import cn.lyf.market.member.service.MemberService;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.R;


/**
 * 会员
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 13:48:04
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
	@Autowired
	private MemberService memberService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("member:member:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = memberService.queryPage(params);

		return R.ok().put("page", page);
	}


	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("member:member:info")
	public R info(@PathVariable("id") Long id) {
		MemberEntity member = memberService.getById(id);

		return R.ok().put("member", member);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("member:member:save")
	public R save(@RequestBody MemberEntity member) {
		memberService.save(member);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("member:member:update")
	public R update(@RequestBody MemberEntity member) {
		memberService.updateById(member);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("member:member:delete")
	public R delete(@RequestBody Long[] ids) {
		memberService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}

	@PostMapping("/register")
	public R register(@RequestBody MemberRegisterVo vo) {
		try {
			memberService.register(vo);
		} catch (PhoneExsitException e) {
			return R.error(BaseCodeEnum.PHONE_EXIST_EXCEPTION);
		} catch (UserNameExistException e) {
			return R.error(BaseCodeEnum.USER_EXIST_EXCEPTION);
		}
		return R.ok();
	}

	@PostMapping("/login")
	public R login(@RequestBody MemberLoginVo vo) {
		MemberEntity user = memberService.login(vo);
		if (user == null) return R.error(BaseCodeEnum.LOGIN_PASSWORD_INVALID_EXCEPTION);
		return R.ok();
	}

}
