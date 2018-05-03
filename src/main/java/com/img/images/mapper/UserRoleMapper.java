package com.img.images.mapper;


import com.img.images.model.UserRole;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleMapper extends BaseMapper<UserRole> {
    void create(UserRole userRole);

    List<UserRole> findByUserId(Long id);
}
