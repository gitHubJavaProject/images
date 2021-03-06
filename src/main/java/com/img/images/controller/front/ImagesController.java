package com.img.images.controller.front;

import com.img.images.model.Image;
import com.img.images.service.ImageFavoriteService;
import com.img.images.service.ImageService;
import com.img.images.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("front/images")
public class ImagesController {

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageFavoriteService imageFavoriteService;

    @Autowired
    private UserService userService;

    @RequestMapping("{id}")
    public ModelAndView getPage(ModelAndView mv, @PathVariable("id") Long id) {
        mv.setViewName("front/image");
        Image image = imageService.get(id);
        if (null==image.getCollectionNumber()) {
            image.setCollectionNumber(1L);
        } else {
            Long n = image.getCollectionNumber()+1;
            image.setCollectionNumber(n);
        }
        imageService.update(image);
        mv.addObject("image", convert(image));
        mv.addObject("images", convert1(imageService.findByKeys(image.getKeys(), image.getId())));
        return mv;
    }

    private List<Map<String, Object>> convert1(List<Image> images) {
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

    private Map<String, Object> convert(Image image) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", image.getId());
        map.put("name", image.getName());
        map.put("description", image.getDescription());
        map.put("typeStr", image.getTypeStr());
        map.put("tag", image.getTag());
        map.put("collectionNumber", image.getCollectionNumber());
        map.put("keys", image.getKeys());
        map.put("width", image.getWidth());
        map.put("height", image.getHeight());
        map.put("showImage", image.getShowImage());
        map.put("fileUrl", image.getFileUrl());
        map.put("downloadNumber", image.getDownloadNumber());
        map.put("favCount", imageFavoriteService.getFavCount(image.getId()));
        map.put("user", userService.getById(image.getUserId()));
        map.put("createDate", image.getCreateDate());
        map.put("pattern", imageService.getPattern(image.getPattern()));
        return map;
    }
}
