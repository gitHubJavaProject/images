package com.img.images.mapper;

import com.img.images.model.Category;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper extends BaseMapper<Category>{
    List<Category> findAll(@Param("name") String name, @Param("level") Integer level);

    List<Category> findByParent(Long id);

    void deleteByParent(Long id);
}
