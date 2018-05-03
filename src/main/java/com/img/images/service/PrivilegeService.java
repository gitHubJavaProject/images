package com.img.images.service;

import com.img.images.mapper.PrivilegeMapper;
import com.img.images.model.Privilege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PrivilegeService {

    @Autowired
    private PrivilegeMapper privilegeMapper;

    public List<Privilege> search(Integer page, Integer size, String name) {
        if (page < 1) {
            page = 1;
        }
        return privilegeMapper.search((page - 1) * size, size, name);
    }

    public Privilege getById(Long id) {
        return privilegeMapper.get(id);
    }

    public List<Privilege> getByParent(Long id) {
        return privilegeMapper.getByParent(id);
    }

    public void delete(Long id) {
        privilegeMapper.delete(id);
    }

    public void create(Privilege privilege) {
        privilegeMapper.save(privilege);
    }

    public void update(Privilege privilege) {
        privilegeMapper.update(privilege);
    }
}
