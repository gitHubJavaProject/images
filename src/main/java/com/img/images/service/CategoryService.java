package com.img.images.service;

import com.img.images.mapper.CategoryMapper;
import com.img.images.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> findAll(String name, Integer level) {
        return categoryMapper.findAll(name, level);
    }

    public void create(Category category) {
        categoryMapper.save(category);
    }

    public void update(Category category) {
        categoryMapper.update(category);
    }

    public Category get(Long id) {
        return categoryMapper.get(id);
    }

    public void delete(Category category) {
        categoryMapper.delete(category.getId());
        if (category.getLevel() == Category.LEVEL_TWO) {
            categoryMapper.deleteByParent(category.getId());
        }
        if (category.getLevel() == Category.LEVEL_ONE) {
            List<Category> list = categoryMapper.findByParent(category.getId());
            for (Category c : list) {
                categoryMapper.deleteByParent(c.getId());
            }
            categoryMapper.deleteByParent(category.getId());
        }
    }

    public List<Category> getByName(String name) {
        return categoryMapper.getByName(name);
    }
}
