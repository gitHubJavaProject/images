package com.img.images.model;

import lombok.Data;

@Data
public class UserRole extends BaseModel{
    private Long userId;
    private Long roleId;
    private String roleName;
}
