package com.img.images.mapper;


import com.img.images.model.RolePrivilege;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePrivilegeMapper extends BaseMapper<RolePrivilege> {

    void deleteByPrivilege(@Param("roleId") Long roleId, @Param("privilegeId") Long privilegeId);

    List<RolePrivilege> findRolePrivilegesByRoleIds(List<Long> roleIds);

    List<RolePrivilege> getByPrivilege(@Param("roleId") Long roleId, @Param("privilegeId") Long privilegeId);
}
