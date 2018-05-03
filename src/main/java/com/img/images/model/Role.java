package com.img.images.model;

import lombok.Data;

@Data
public class Role extends BaseModel {
    public static final int BUSINESS = 1;
    public static final int FRONT = 2;
    private String name;
    private String description;
}
