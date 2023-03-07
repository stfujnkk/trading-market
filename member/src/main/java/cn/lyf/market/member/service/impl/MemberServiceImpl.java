package cn.lyf.market.member.service.impl;

import cn.lyf.market.member.dao.MemberLevelDao;
import cn.lyf.market.member.entity.MemberLevelEntity;
import cn.lyf.market.member.exception.PhoneExsitException;
import cn.lyf.market.member.exception.UserNameExistException;
import cn.lyf.market.member.vo.MemberLoginVo;
import cn.lyf.market.member.vo.MemberRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.common.utils.Query;

import cn.lyf.market.member.dao.MemberDao;
import cn.lyf.market.member.entity.MemberEntity;
import cn.lyf.market.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

	@Autowired
	MemberLevelDao memberLevelDao;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<MemberEntity> page = this.page(
				new Query<MemberEntity>().getPage(params),
				new QueryWrapper<MemberEntity>()
		);

		return new PageUtils(page);
	}

	@Override
	public void register(MemberRegisterVo vo) {
		checkPhoneUnique(vo.getPhone());
		checkUserNameUnique(vo.getUserName());

		MemberEntity entity = new MemberEntity();
		MemberDao memberDao = this.baseMapper;

		MemberLevelEntity levelEntity = memberLevelDao.getDefaultLevel();
		if (levelEntity != null) entity.setLevelId(levelEntity.getId());

		entity.setMobile(vo.getPhone());
		entity.setUsername(vo.getUserName());

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodePassWord = encoder.encode(vo.getPassWord() + "$" + vo.getUserName());
		entity.setPassword(encodePassWord);

		memberDao.insert(entity);
	}

	@Override
	public void checkPhoneUnique(String phone) {
		MemberDao memberDao = this.baseMapper;
		Integer cnt = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
		if (cnt > 0) throw new PhoneExsitException();
	}

	@Override
	public void checkUserNameUnique(String userName) {
		MemberDao memberDao = this.baseMapper;
		Integer cnt = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
		if (cnt > 0) throw new UserNameExistException();
	}

	@Override
	public MemberEntity login(MemberLoginVo vo) {
		String account = vo.getAccount();
		String password = vo.getPassword();
		MemberDao memberDao = this.baseMapper;
		MemberEntity memberEntity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("username", account).or().eq("mobile", account));
		if (memberEntity == null) return null;
		String passwordHash = memberEntity.getPassword();
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		boolean matches = passwordEncoder.matches(password + "$" + memberEntity.getUsername(), passwordHash);
		if (!matches) return null;
		return memberEntity;
	}
}
