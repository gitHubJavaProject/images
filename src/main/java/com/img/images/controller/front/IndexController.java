package com.img.images.controller.front;

import com.img.images.controller.BaseController;
import com.img.images.model.Category;
import com.img.images.model.Image;
import com.img.images.model.User;
import com.img.images.service.*;
import com.img.images.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping("index")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("front/index");
        List<Category> categories = categoryService.findAll(null, null);
        mv.addObject("categories", categories);
        List<Image> images = imageService.findByDownloadNumber();
        mv.addObject("images", images);
        return mv;
    }

    @RequestMapping("selectKey")
    public R keys(String param) {
        return R.ok(200, "success").put("keys", selectKeyService.find(param));
    }

    @RequestMapping("list")
    public ModelAndView list(ModelAndView mv, String param) {
        mv.setViewName("front/index");
        return mv;
    }

    @RequestMapping("myself")
    public ModelAndView myself(ModelAndView mv) {
        mv.setViewName("front/user_self_center");
        Integer countFav = favoriteService.countFavByUserId(getLoginUser().getId());
        mv.addObject("countFav", countFav);
        Integer countImg = imageService.countImgByUserId(getLoginUser().getId());
        mv.addObject("countImg", countImg);
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
    public R updateUser(@PathVariable("id") Long id, User user) {
        User oldUser = userService.getById(id);
        if (null == oldUser) {
            return R.error(404, "数据不存在！").put("icon", "warning");
        }
        oldUser.setName(user.getName());
        oldUser.setPwd(user.getPwd());
        userService.update(oldUser);
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

    @DeleteMapping("images/delete/{id}")
    public R deleteImage(@PathVariable("id") Long id) {
        Image image = imageService.get(id);
        if (null == image) {
            return R.error(404, "数据不存在！").put("icon", "warning");
        }
        if (image.getUserId() != getLoginUser().getId().longValue()) {
            return R.error(403, "没有此数据权限！").put("icon", "warning");
        }
        imageService.delete(id);
        return R.ok(204, "删除成功！").put("icon", "success");
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
        return R.ok(204, "提交成功！").put("icon", "warning");
    }
}
