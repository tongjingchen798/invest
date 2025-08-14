

package io.renren.dao;

import io.renren.common.dao.BaseDao;
import io.renren.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户
 * 
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface UserDao extends BaseDao<UserEntity> {
    UserEntity getUserByMobile(String mobile);

    UserEntity getUserByUserId(Long userId);

    @Select("select * from tb_user where invite_code = #{inviteCode}")
    UserEntity getUserByInviteCode(@Param("inviteCode") String inviteCode);

    /**
     * 根据上级邀请码查询用户列表
     * @param upinviteCode 上级邀请码
     * @return 用户列表
     */
    List<UserEntity> selectBySuperiorId(@Param("upinviteCode") String upinviteCode);
}
