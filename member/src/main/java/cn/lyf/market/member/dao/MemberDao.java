package cn.lyf.market.member.dao;

import cn.lyf.market.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author lyf
 * @email 3365310020@qq.com
 * @date 2022-05-28 12:24:10
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
