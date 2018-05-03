package com.img.images.mapper;


import com.img.images.model.Privilege;
import com.img.images.model.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RoleMapper extends BaseMapper<Role>{

    List<Privilege> getPrivilegesByRole(Long id);

    List<Role> search(@Param("offset") Integer offset, @Param("size") Integer size, @Param("name") String name);

    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    Role selectByPrimaryKey(Long id);

    List<Role> findRolesByUserId(Long userId);

    List<Role> findAll();
}
