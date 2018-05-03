package com.img.images.mapper;


import com.img.images.model.Privilege;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeMapper extends BaseMapper<Privilege> {

    List<Privilege> getByParent(Long id);

    List<Privilege> getAllPrivileges();

    List<Privilege> search(@Param("offset") Integer offset, @Param("size") Integer size, @Param("name") String name);

    int deleteByPrimaryKey(Long id);

    int insert(Privilege record);

    Privilege selectByPrimaryKey(Long id);

    List<Privilege> findPrivilegesByIds(List<Long> ids);
}
