package com.img.images.model;

import lombok.Data;

@Data
public class RolePrivilege extends BaseModel {
    private Long roleId;
    private Long privilegeId;
}
