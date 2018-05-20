package com.img.images.service;

import com.img.images.mapper.SelectKeyMapper;
import com.img.images.model.SelectKey;
import org.apache.commons.lang3.StringUtils;
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

    public void update(String key) {
        SelectKey selectKey1 = selectKeyMapper.getByKey(key);
        if (StringUtils.isNotBlank(key) && null == selectKey1) {
            SelectKey selectKey = new SelectKey();
            selectKey.setSelectNumber(0L);
            selectKey.setKey(key);
        } else {
            if (null != selectKey1) {
                selectKey1.setSelectNumber(selectKey1.getSelectNumber()+1);
                selectKeyMapper.update(selectKey1);
            }
        }
    }
}
