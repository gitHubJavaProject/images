package com.img.images.model;

import java.util.List;

public class PrivilegeChecker {
    private final List<Privilege> privilegs;

    public PrivilegeChecker(List<Privilege> privilegs) {
        this.privilegs = privilegs;
    }

    public boolean hasPrivilege(String dirUrl) {
        if (null == dirUrl) {
            return false;
        }
        for (Privilege privilege : this.privilegs) {
            if (dirUrl.contains("/" + privilege.getResource() + "/")) {
                if (dirUrl.contains("/" + privilege.getAction() + "/") || dirUrl.contains("/" + privilege.getAction() + "?")) {
                    return true;
                }
            }
            if (dirUrl.contains("/_/")) {
                return true;
            }
        }
        return false;
    }
}
