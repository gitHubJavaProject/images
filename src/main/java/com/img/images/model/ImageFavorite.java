package com.img.images.model;

import lombok.Data;

@Data
public class ImageFavorite extends BaseModel {
    private Long favoriteId;
    private Long imageId;
}
