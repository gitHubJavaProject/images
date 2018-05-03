package com.img.images.service;

import com.img.images.mapper.CategoryMapper;
import com.img.images.mapper.ImageMapper;
import com.img.images.model.Category;
import com.img.images.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public List<Image> search(Integer page, Integer size, String name) {
        if (page < 1) {
            page = 1;
        }
        return imageMapper.search((page - 1) * size, size, name);
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
}
