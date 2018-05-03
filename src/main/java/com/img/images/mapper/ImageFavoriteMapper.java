package com.img.images.mapper;

import com.img.images.model.Image;
import com.img.images.model.ImageFavorite;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageFavoriteMapper extends BaseMapper<ImageFavorite> {
    List<Image> findByFavoriteId(Long id);
}
