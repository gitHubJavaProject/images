package com.img.images.controller.front;

import com.img.images.controller.BaseController;
import com.img.images.model.Category;
import com.img.images.model.Image;
import com.img.images.model.PatternService;
import com.img.images.model.User;
import com.img.images.service.*;
import com.img.images.util.FinalKeys;
import com.img.images.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("frontIndexController")
@RequestMapping("front")
public class IndexController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private SelectKeyService selectKeyService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private ImageFavoriteService imageFavoriteService;

    @Autowired
    private PatternService patternService;

    @RequestMapping("index")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("front/index");
        List<Image> images = imageService.list(1, 4, null, null, null, 3, null);
        mv.addObject("imagesFav", convert(images));
        List<Image> images2 = imageService.list(1, 4, null, null, null, 2, null);
        mv.addObject("imagesDown", convert(images2));
        List<Image> images1 = imageService.list(1, 4, null, null, null, 1, null);
        mv.addObject("imagesNew", convert(images1));
        return mv;
    }

    @RequestMapping("selectKey")
    public R keys(String param) {
        return R.ok(200, "success").put("keys", selectKeyService.find(param));
    }

    @RequestMapping("list")
    public ModelAndView list(ModelAndView mv,
                             @RequestParam(value = "params", required = false) String params,
                             @RequestParam(value = "category", required = false) String category,
                             @RequestParam(value = "pattern", required = false) Integer pattern,
                             @RequestParam(value = "tag", required = false) Integer tag,
                             @RequestParam(value = "order", required = false) Integer order,
                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "16") Integer size) {
        mv.setViewName("front/list");
        List<Image> images = imageService.list(page, size, params, category, tag, order, pattern);
        Integer countTotal = imageService.countTotal(params, category, tag, pattern);
        mv.addObject("patterns", patternService.findAll());
        mv.addObject("pattern", pattern);
        mv.addObject("images", convert(images));
        mv.addObject("page", page);
        mv.addObject("size", size);
        mv.addObject("params", null == params? "" : params);
        mv.addObject("category", category);
        mv.addObject("tag", tag);
        mv.addObject("order", order);
        mv.addObject("countPage", countTotal%size>0?countTotal/size+1:countTotal/size);
        mv.addObject("countTotal", countTotal);
        List<Category> categories = categoryService.findAll(null, 1);
        if (StringUtils.isNotBlank(category)) {
            List<Category> category1 = categoryService.getByName((category.contains(">") ? category.substring(category.lastIndexOf(">")+1):category));
            if ((category1.get(0).getLevel()+1) > 3) {
                categories = categoryService.findAll1(null, 3, category1.get(0).getParent());
            } else {
               // System.out.println(category1.get(0).getId());
                categories = categoryService.findAll1(null, category1.get(0).getLevel()+1, category1.get(0).getId());
            }

        }
        mv.addObject("categories", categories);
        return mv;
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

    @RequestMapping("p/myself")
    public ModelAndView myself(ModelAndView mv) {
        mv.setViewName("front/user_self_center");
        Integer countFav = favoriteService.countFavByUserId(getLoginUser().getId());
        mv.addObject("countFav", countFav);
        Integer countImg = imageService.countImgByUserId(getLoginUser().getId());
        mv.addObject("countImg", countImg);
        mv.addObject("patterns", patternService.findAll());
        //List<Favorite> favorites = favoriteService.findByUserId(1L);
        // mv.addObject("favorites", favorites);
        return mv;
    }

    @RequestMapping("lists")
    public Object lists(@RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer size,
                        @RequestParam(value = "name", required = false) String name) {
        List<Image> images = imageService.search(page, size, name, getLoginUser().getId());
        Map<String, Object> result = new HashMap<>();
        result.put("images", images);
        result.put("page", page);
        result.put("size", size);
        result.put("name", null == name ? "" : name);
        result.put("count", imageService.count());
        return result;
    }

    @PutMapping("update/{id}")
    public R updateUser(@PathVariable("id") Long id, User user, HttpServletRequest request) {
        User oldUser = userService.getById(id);
        if (null == oldUser) {
            return R.error(404, "数据不存在！").put("icon", "warning");
        }
        oldUser.setName(user.getName());
        oldUser.setPwd(user.getPwd());
        oldUser.setHeader(user.getHeader());
        userService.update(oldUser);
        request.getSession().setAttribute(FinalKeys.LOGIN_USER_KEY, userService.getByUserName(oldUser.getUserName()));
        return R.ok(204, "成功！").put("user", user).put("icon", "success");
    }

    @RequestMapping("categories")
    public Object getAll() {
        return categoryService.findAll(null, null);
    }

    @RequestMapping("image/{id}")
    public Object getImg(@PathVariable("id") Long id) {
        return imageService.get(id);
    }

    @RequestMapping("images/{id}/getFavCount")
    public Object getFavCount(@PathVariable("id") Long id) {
        return imageFavoriteService.getFavCount(id);
    }

    @RequestMapping("imageFavorites/del")
    public R imageFavoritesDel(Long imageId, Long favoriteId) {
        imageFavoriteService.delete(imageId, favoriteId);
        return R.ok(204, "删除成功！");
    }

    @RequestMapping("imageFavorites/add")
    public R imageFavoriteAdd(Long imageId, Long favoriteId) {
        imageFavoriteService.save(imageId, favoriteId);
        return R.ok(201, "添加成功！");
    }

    @RequestMapping("images/{id}/toApproval")
    public R toApproval(@PathVariable("id") Long id) {
        Image image = imageService.get(id);
        if (null == image) {
            return R.error(404, "无此条数据！").put("icon", "warning");
        }
        if (image.getStatus() != 1 && image.getStatus() != 5) {
            return R.error(403, "当前状态不能提交！").put("icon", "warning");
        }
        image.setStatus(2);
        imageService.update(image);
        return R.ok(204, "提交成功！").put("icon", "success");
    }

}
