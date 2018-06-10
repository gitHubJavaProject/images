package com.img.images.controller.front;

import com.img.images.controller.BaseController;
import com.img.images.model.Image;
import com.img.images.service.ImageFavoriteService;
import com.img.images.service.ImageService;
import com.img.images.util.R;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("frontImageController")
@RequestMapping("front/images")
public class ImageController extends BaseController{
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
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

    @PostMapping("add")
    public R createImage(Image image) {
        try {
            if (StringUtils.isBlank(image.getName()) || StringUtils.isBlank(image.getShowImage())
                    || StringUtils.isBlank(image.getFileUrl()) || null == image.getWidth()
                    || null == image.getHeight() || null == image.getTag() || null == image.getType()) {
                return R.error(400, "参数不正确！").put("icon", "warning");
            }
            if (null == getLoginUser()) {
                return R.error(403, "请先登录！").put("icon", "warning");
            }
            image.setUserId(getLoginUser().getId());
            image.setStatus(1);
            imageService.save(image);
            return R.ok(201, "保存成功！").put("icon", "success").put("image", image);
        } catch (Exception e) {
            LOGGER.error("系统内部错误！", e);
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }

    @PutMapping("update/{id}")
    public R updateImage(Image image, @PathVariable("id") Long id) {
        try {
            if (StringUtils.isBlank(image.getName()) || StringUtils.isBlank(image.getShowImage())
                    || StringUtils.isBlank(image.getFileUrl()) || null == image.getWidth()
                    || null == image.getHeight() || null == image.getTag() || null == image.getType()) {
                return R.error(400, "参数不正确！").put("icon", "warning");
            }
            Image oldImage = imageService.get(id);
            if (null == oldImage) {
                return R.error(404, "此数据不存在！").put("icon", "warning");
            }
            if (null == getLoginUser()) {
                return R.error(403, "请先登录！").put("icon", "warning");
            }
            if (getLoginUser().getId().longValue() != oldImage.getUserId()) {
                return R.error(403, "不能操作别人的数据！").put("icon", "warning");
            }
            oldImage.setTypeStr(image.getTypeStr());
            oldImage.setType(image.getType());
            oldImage.setShowImage(image.getShowImage());
            oldImage.setFileUrl(image.getFileUrl());
            oldImage.setDescription(image.getDescription());
            oldImage.setHeight(image.getHeight());
            oldImage.setName(image.getName());
            oldImage.setWidth(image.getWidth());
            oldImage.setTag(image.getTag());
            oldImage.setKeys(image.getKeys());
            imageService.update(oldImage);
            return R.ok(204, "保存成功！").put("icon", "success");
        } catch (Exception e) {
            LOGGER.error("系统内部错误！", e);
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }

    @DeleteMapping("delete/{id}")
    public R deleteImage(@PathVariable("id") Long id) {
        try {
            Image image = imageService.get(id);
            if (null == image) {
                return R.error(404, "数据不存在！").put("icon", "warning");
            }
            if (null == getLoginUser()) {
                return R.error(403, "请先登录！").put("icon", "warning");
            }
            if (getLoginUser().getId().longValue() != image.getUserId()) {
                return R.error(403, "不能操作别人的数据！").put("icon", "warning");
            }
            imageService.delete(id);
            return R.ok(204, "操作成功！").put("icon", "success");
        } catch (Exception e) {
            LOGGER.error("系统内部错误！", e);
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }
}
