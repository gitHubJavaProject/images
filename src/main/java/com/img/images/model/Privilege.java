package com.img.images.model;

import lombok.Data;

@Data
public class Privilege extends BaseModel {
    private String name;
    private String resource;
    private String action;
    private Long parentId;
}
