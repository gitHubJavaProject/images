package com.img.images.controller.front;

import com.img.images.controller.BaseController;
import com.img.images.model.Favorite;
import com.img.images.model.Image;
import com.img.images.service.FavoriteService;
import com.img.images.service.ImageFavoriteService;
import com.img.images.service.ImageService;
import com.img.images.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("front/p/favorites")
public class FavoriteController extends BaseController{
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ImageFavoriteService imageFavoriteService;

    @Autowired
    private ImageService imageService;

    @RequestMapping("")
    public Object list(Integer page, Integer size) {
        Map<String, Object> map = new HashMap<>();
        map.put("favorites", favoriteService.search(page, size, getLoginUser().getId()));
        map.put("page", page);
        map.put("size", size);
        map.put("count", favoriteService.findByUserId(getLoginUser().getId()).size());
        return map;
    }

    @PostMapping("")
    public R add(Favorite favorite) {
        Favorite favorite1 = favoriteService.getByName(favorite.getName());
        if (null != favorite1) {
           return R.error(403,"名称重复").put("icon", "warming");
        }
        favorite.setUserId(getLoginUser().getId());
        favoriteService.save(favorite);
        return R.ok(201, "保存成功！").put("icon", "success").put("favorite", favorite);
    }

    @PutMapping("{id}")
    public R update(Favorite favorite, @PathVariable("id") Long id) {
        Favorite favorite1 = favoriteService.get(id);
        if (null == favorite1) {
            return R.error(404, "数据不存在！").put("icon", "warning");
        }
        if (favorite1.getUserId().longValue() != getLoginUser().getId()) {
            return R.error(403, "没有此数据权限！").put("icon", "warning");
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
        mv.addObject("images", convert(images));
        Favorite favorite = favoriteService.get(id);
        mv.addObject("favorite", favorite);
        return mv;
    }

    @RequestMapping("findAll")
    public Object findAll() {
        return favoriteService.findAll(getLoginUser().getId());
    }

    @RequestMapping("byImageId/{id}")
    public Object byImage(@PathVariable("id") Long id) {
        return favoriteService.byImage(id, getLoginUser().getId());
    }

    @PostMapping("addImage")
    @Transactional
    public R addImage(String name, Long imageId) {
        Favorite favorite = new Favorite();
        favorite.setName(name);
        favorite.setUserId(getLoginUser().getId());
        favoriteService.save(favorite);
        imageFavoriteService.save(imageId, favorite.getId());
        return R.ok(201, "创建成功！").put("favorite", favorite).put("icon", "success");
    }

    private List<Map<String, Object>> convert(List<Image> images) {
        List<Map<String, Object>> list = new ArrayList<>();
        for(Image image:images) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", image.getId());
            map.put("name", image.getName());
            map.put("showImage", image.getShowImage());
            map.put("fileUrl", image.getFileUrl());
            map.put("downloadNumber", image.getDownloadNumber());
            map.put("favCount", imageFavoriteService.getFavCount(image.getId()));
            list.add(map);
        }
        return list;
    }

}
