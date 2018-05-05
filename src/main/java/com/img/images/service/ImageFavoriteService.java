package com.img.images.service;

import com.img.images.mapper.ImageFavoriteMapper;
import com.img.images.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImageFavoriteService {
    @Autowired
    private ImageFavoriteMapper imageFavoriteMapper;

    public List<Image> findByFavoriteId(Long id) {
        return imageFavoriteMapper.findByFavoriteId(id);
    }

    public void save(Long imageId, Long favoriteId) {
        imageFavoriteMapper.create(imageId, favoriteId);
    }

    public Integer getFavCount(Long id) {
        return imageFavoriteMapper.getFavCount(id);
    }

    public void delete(Long imageId, Long favoriteId) {
        imageFavoriteMapper.deleteByImgIdAndFavId(imageId, favoriteId);
    }
}
