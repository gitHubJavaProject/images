package com.img.images.service;

import com.img.images.mapper.CategoryMapper;
import com.img.images.mapper.ImageMapper;
import com.img.images.mapper.SelectKeyMapper;
import com.img.images.model.Category;
import com.img.images.model.Image;
import com.img.images.model.SelectKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class ImageService {

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private SelectKeyMapper selectKeyMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    public Integer count() {
        return imageMapper.count();
    }

    public void addDownloadNumber (Image image) {
        imageMapper.update(image);
    }

    public void delete(Long id) {
        imageMapper.delete(id);
    }

    public List<Image> search(Integer page, Integer size, String name, Long userId) {
        if (page < 1) {
            page = 1;
        }
        return imageMapper.search((page - 1) * size, size, name, userId);
    }

    public Image get(Long id) {
        return imageMapper.get(id);
    }

    public void save(Image image) {
        String typeStr = getStr(image);
        image.setTypeStr(typeStr);
        image.setDownloadNumber(0L);
        updateKeys(image);
        imageMapper.save(image);
    }

    private void updateKeys(Image image) {
        String[] keys = image.getKeys().split("#");
        for (String key : keys) {
           if (StringUtils.isNotBlank(key) && null == selectKeyMapper.getByKey(key)) {
               SelectKey selectKey = new SelectKey();
               selectKey.setKey(key);
               selectKey.setSelectNumber(0L);
               selectKeyMapper.save(selectKey);
           }
        }
    }

    public void update(Image image) {
        String typeStr = getStr(image);
        image.setTypeStr(typeStr);
        updateKeys(image);
        imageMapper.update(image);
    }

    private String getStr(Image image) {
        Category category = categoryMapper.get(image.getType());
        String typeStr = category.getName();
        if (category.getParent() != 0) {
            category = categoryMapper.get(category.getParent());
            typeStr = category.getName() + ">" + typeStr;
        }
        if (category.getParent() != 0) {
            category = categoryMapper.get(category.getParent());
            typeStr = category.getName() + ">" + typeStr;
        }
        return typeStr;
    }

    public List<Image> findByDownloadNumber() {
        return imageMapper.findByDownloadNumber();
    }

    public Integer countImgByUserId(Long id) {
        return imageMapper.countImgByUserId(id);
    }

    public Integer countTotal(String param, String category, Integer tag, Integer pattern) {
        return imageMapper.countTotal(param, category, tag, pattern);
    }

    public List<Image> list(Integer page, Integer size, String param, String category, Integer tag, Integer order, Integer pattern) {
        if (page < 1) {
            page = 1;
        }
        return imageMapper.list((page - 1) * size, size, param, category, tag, order, pattern);
    }

    public List<Image> getByTypeStr(String typeStr) {
        return imageMapper.getByTypeStr(typeStr);
    }

    public List<Image> findByKeys(String keys, Long id) {
        String[] keys1 = keys.substring(1,keys.length()-1).split("#");
        List<Image> list = new ArrayList<>();
        for (String key:keys1) {
            list.addAll(imageMapper.findByKeys(key, id));
        }
        Set<Image> set = new TreeSet<Image>(new Comparator<Image>() {
            @Override
            public int compare(Image o1, Image o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        set.addAll(list);
        List<Image> list1 =  new ArrayList<>(set);
        return list1.size()>8? list1.subList(0,8): list1;
    }

    public String getPattern(Integer id) {
        return imageMapper.getPattern(id);
    }
}
