package cn.lyf.market.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lyf.common.utils.PageUtils;
import cn.lyf.market.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:24:10
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

