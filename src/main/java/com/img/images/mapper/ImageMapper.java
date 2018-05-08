package com.img.images.mapper;

import com.img.images.model.Image;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageMapper extends BaseMapper<Image>{

    Integer count();

    List<Image> search(@Param("offset") Integer offset, @Param("size") Integer size, @Param("name") String name, @Param("userId") Long userId);

    List<Image> findByDownloadNumber();

    Integer countImgByUserId(Long id);

    Integer countTotal(@Param("params") String params,
                       @Param("category") String category,
                       @Param("tag") Integer tag);

    List<Image> list(@Param("offset") Integer offset,
                     @Param("size") Integer size,
                     @Param("params") String params,
                     @Param("category") String category,
                     @Param("tag") Integer tag,
                     @Param("order") Integer order);
    List<Image> getByTypeStr(String typeStr);

    List<Image> findByKeys(@Param("key") String  key, @Param("id") Long id);
}
