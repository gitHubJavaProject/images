package com.img.images.service;

import com.img.images.mapper.SelectKeyMapper;
import com.img.images.model.SelectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class SelectKeyService {
    @Autowired
    private SelectKeyMapper selectKeyMapper;

    public List<SelectKey> find(String param){
        return selectKeyMapper.find(param);
    }
}
