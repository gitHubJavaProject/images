package com.img.images.mapper;

import com.img.images.model.FavAndCountImg;
import com.img.images.model.Favorite;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteMapper extends BaseMapper<Favorite> {
    Favorite getByName(String name);

    List<Favorite> findByUserId(Long id);

    List<Favorite> search(@Param("offset") Integer offset, @Param("size") Integer size, @Param("userId") Long userId);

    Integer countFavByUserId(Long id);

    List<Favorite> findFavImgByImgId(@Param("id") Long id, @Param("userId") Long userId);

    List<FavAndCountImg> findFavAndCountImgByUserId(Long id);
}
