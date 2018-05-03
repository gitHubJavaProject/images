package com.img.images.model;

import lombok.Data;

@Data
public class Favorite extends BaseModel {
    private String name;
    private String description;
    private String url;
    private Long userId;
}
