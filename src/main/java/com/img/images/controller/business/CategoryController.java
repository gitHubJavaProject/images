package com.img.images.controller.business;

import com.img.images.model.Category;
import com.img.images.service.CategoryService;
import com.img.images.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("business/p/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("list")
    public ModelAndView page(ModelAndView mv,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "level", required = false) Integer level) {
        mv.setViewName("business/protect/category");
        List<Category> categories = categoryService.findAll(name, level);
        mv.addObject("categoriesOne", filter(categories, Category.LEVEL_ONE));
        mv.addObject("categoriesTwo", filter(categories, Category.LEVEL_TWO));
        mv.addObject("categoriesThree", filter(categories, Category.LEVEL_THREE));
        return mv;
    }

    @PostMapping("add")
    public R create(Category category) {
        try {
            Category parent = categoryService.get(category.getParent());
            if (category.getParent() != 0 && null == parent) {
                return R.error(404, "上一级不存在，请重新选择！").put("icon", "warning");
            }
            if (null != categoryService.getByName(category.getName()) && categoryService.getByName(category.getName()).size() > 0) {
                return R.error(404, "已存在！").put("icon", "warning");
            }
            categoryService.create(category);
            return R.ok(201, "保存成功！").put("icon", "success").put("category", category);
        } catch (Exception e) {
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }

    @PutMapping("update/{id}")
    public R update(@PathVariable("id") Long id, Category category) {
        try {
            Category oldCategory = categoryService.get(id);
            if (null == oldCategory) {
                return R.error(404, "未找到数据！").put("icon", "warning");
            }
            if (null != categoryService.getByName(category.getName()) && categoryService.getByName(category.getName()).size() > 0
                    && categoryService.getByName(category.getName()).get(0).getId() != oldCategory.getId().longValue()) {
                return R.error(404, "已存在！").put("icon", "warning");
            }
            oldCategory.setName(category.getName());
            oldCategory.setDescription(category.getDescription());
            categoryService.update(oldCategory);
            return R.ok(204, "修改成功！").put("icon", "success");
        } catch (Exception e) {
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }

    @DeleteMapping("delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        try {
            Category oldCategory = categoryService.get(id);
            if (null == oldCategory) {
                return R.error(404, "此数据不存在！").put("icon", "warning");
            }
            categoryService.delete(oldCategory);
            return R.ok(204, "删除成功！").put("icon", "success");
        } catch (Exception e) {
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }

    private List<Category> filter(List<Category> categories, int level) {
        List<Category> categoriesLevel = new ArrayList<>();
        for (Category category : categories) {
            if(category.getLevel() == level) {
                categoriesLevel.add(category);
            }
        }
        return categoriesLevel;
    }
}
