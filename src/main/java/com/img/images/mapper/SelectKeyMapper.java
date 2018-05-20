package com.img.images.mapper;


import com.img.images.model.SelectKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectKeyMapper extends BaseMapper<SelectKey> {

    List<SelectKey> find(@Param("param") String param);

    SelectKey getByKey(String key);
}
