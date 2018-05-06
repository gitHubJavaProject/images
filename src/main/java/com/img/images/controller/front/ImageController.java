package com.img.images.controller.front;

import com.img.images.model.Image;
import com.img.images.service.ImageFavoriteService;
import com.img.images.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("frontImageController")
@RequestMapping("front/images")
public class ImageController {
    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageFavoriteService imageFavoriteService;

    @RequestMapping("findByDownloadNumber")
    public Object findByDownloadNumber() {
        List<Image> images = imageService.findByDownloadNumber();
        return convert(images);
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
