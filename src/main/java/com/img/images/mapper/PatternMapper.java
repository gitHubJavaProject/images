package com.img.images.mapper;

import com.img.images.model.Pattern;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatternMapper extends BaseMapper<Pattern> {
    List<Pattern> findAll();
}
