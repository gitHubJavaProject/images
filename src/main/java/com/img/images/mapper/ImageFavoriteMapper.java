package com.img.images.mapper;

import com.img.images.model.Image;
import com.img.images.model.ImageFavorite;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageFavoriteMapper extends BaseMapper<ImageFavorite> {
    List<Image> findByFavoriteId(Long id);
    void create(@Param("imageId") Long imageId, @Param("favoriteId") Long favoriteId);
    Integer getFavCount(Long id);
    void deleteByImgIdAndFavId(@Param("imageId") Long imageId, @Param("favoriteId") Long favoriteId);
}
