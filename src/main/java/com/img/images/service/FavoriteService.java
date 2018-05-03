package com.img.images.service;

import com.img.images.mapper.FavoriteMapper;
import com.img.images.model.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FavoriteService {

    @Autowired
    private FavoriteMapper favoriteMapper;

    public void save(Favorite favorite) {
        favoriteMapper.save(favorite);
    }

    public void update(Favorite favorite) {
        favoriteMapper.update(favorite);
    }

    public Favorite getByName(String name) {
        return favoriteMapper.getByName(name);
    }

    public List<Favorite> findByUserId(Long id) {
        return favoriteMapper.findByUserId(id);
    }

    public Favorite get(Long id) {
        return favoriteMapper.get(id);
    }

    public List<Favorite> search(Integer page, Integer size, Long userId) {
        if (page<1) {
            page = 1;
        }
        return favoriteMapper.search(size*(page-1), size, userId);
    }

    public Integer countFavByUserId(Long id) {
        return favoriteMapper.countFavByUserId(id);
    }
}
