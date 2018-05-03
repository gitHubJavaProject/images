package com.img.images.model;

import lombok.Data;

@Data
public class Category extends BaseModel {
    public static final int LEVEL_ONE = 1;
    public static final int LEVEL_TWO = 2;
    public static final int LEVEL_THREE = 3;
    private String name;
    private String description;
    private Long parent;
    private Integer level;

}
