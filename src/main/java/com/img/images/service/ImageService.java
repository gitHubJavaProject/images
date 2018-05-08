package com.img.images.service;

import com.img.images.mapper.CategoryMapper;
import com.img.images.mapper.ImageMapper;
import com.img.images.model.Category;
import com.img.images.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class ImageService {

    @Autowired
    private ImageMapper imageMapper;

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
        imageMapper.save(image);
    }

    public void update(Image image) {
        String typeStr = getStr(image);
        image.setTypeStr(typeStr);
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

    public Integer countTotal(String param, String category, Integer tag) {
        return imageMapper.countTotal(param, category, tag);
    }

    public List<Image> list(Integer page, Integer size, String param, String category, Integer tag, Integer order) {
        if (page < 1) {
            page = 1;
        }
        return imageMapper.list((page - 1) * size, size, param, category, tag, order);
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
        return list.size()>8? list.subList(0,8): list;
    }
}
