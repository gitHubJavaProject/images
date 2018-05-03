package com.img.images.controller.front;

import com.img.images.model.Favorite;
import com.img.images.model.Image;
import com.img.images.service.FavoriteService;
import com.img.images.service.ImageFavoriteService;
import com.img.images.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("front/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ImageFavoriteService imageFavoriteService;

    @RequestMapping("")
    public Object list(Integer page, Integer size) {
        Map<String, Object> map = new HashMap<>();
        map.put("favorites", favoriteService.search(page, size, 1L));
        map.put("page", page);
        map.put("size", size);
        map.put("count", favoriteService.findByUserId(1L).size());
        return map;
    }

    @PostMapping("")
    public R add(Favorite favorite) {
        Favorite favorite1 = favoriteService.getByName(favorite.getName());
        if (null != favorite1) {
           return R.error(403,"名称重复").put("icon", "warming");
        }
        favorite.setUserId(1L);
        favoriteService.save(favorite);
        return R.ok(201, "保存成功！").put("icon", "success").put("favorite", favorite);
    }

    @PutMapping("{id}")
    public R update(Favorite favorite, @PathVariable("id") Long id) {
        Favorite favorite1 = favoriteService.get(id);
        if (null == favorite1) {
            return R.error(404, "数据不存在！").put("icon", "warning");
        }
        favorite1.setName(favorite.getName());
        favorite1.setDescription(favorite.getDescription());
        favorite1.setUrl(favorite.getUrl());
        favoriteService.update(favorite1);
        return R.ok(204, "保存成功！").put("icon", "success");
    }

    @RequestMapping("{id}")
    public ModelAndView findById(ModelAndView mv, @PathVariable("id") Long id) {
        mv.setViewName("front/protect/favorite");
        List<Image> images = imageFavoriteService.findByFavoriteId(id);
        mv.addObject("images", images);
        Favorite favorite = favoriteService.get(id);
        mv.addObject("favorite", favorite);
        return mv;
    }
}
