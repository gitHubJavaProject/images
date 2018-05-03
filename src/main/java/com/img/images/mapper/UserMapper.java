package com.img.images.mapper;

import com.img.images.model.Privilege;
import com.img.images.model.User;
import com.img.images.model.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    List<UserRole> getUserRoleByUserId(Long userId);

    void update(User user);

    void startOrStopUser(User user);

    List<User> search(@Param("offset") Integer offset, @Param("size") Integer size, @Param("name") String name);

    User getByUserName(@Param("userName") String userName);

    void create(User user);

    User getByMobile(@Param("mobile") String mobile);

    User getByName(String name);

    User getById(@Param("id") Long id);

    boolean updatePwd(@Param("id") Long id, @Param("salt") String salt, @Param("pwd") String pwd);

    void updatePwdAndPhone(User user);

    List<User> getByOrgId(Integer orgId);

    List<Privilege> getPrivilegesByUser(Long id);
}
