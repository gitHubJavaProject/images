package com.img.images.model;

import java.util.List;

public class RoleChecker {
    private final List<Role> roles;

    public RoleChecker(List<Role> roles) {
        this.roles = roles;
    }

    public boolean hasRole(Long roleId) {
        if (null == roleId) {
            return false;
        }
        for (Role role : this.roles) {
            if (role.getId() == roleId.longValue()) {
                return true;
            }
        }
        return false;
    }
}
