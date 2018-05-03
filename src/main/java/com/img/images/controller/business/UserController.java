package com.img.images.controller.business;

import com.img.images.model.Role;
import com.img.images.model.User;
import com.img.images.model.UserRole;
import com.img.images.service.RoleService;
import com.img.images.service.UserService;
import com.img.images.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("business/p/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("list")
    public ModelAndView list(ModelAndView mv,
                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                             @RequestParam(value = "name", required = false) String name) {
        mv.setViewName("business/protect/users");
        List<User> users = userService.search(page, size, name);
        mv.addObject("name", null == name ? "" : name);
        mv.addObject("page", page);
        mv.addObject("size", size);
        mv.addObject("users", convertUser(users));
        return mv;
    }

    @RequestMapping("list/{id}")
    public ModelAndView upUserPage(ModelAndView mv, @PathVariable("id") Long id) {
        mv.setViewName("business/protect/upUser");
        User user = userService.getById(id);
        mv.addObject("user", user);
        return mv;
    }

    @PutMapping("update/{id}")
    public R updateUser(@PathVariable("id") Long id, User user) {
        try {
            User oldUser = userService.getById(id);
            if (null == oldUser) {
                return R.error(404, "数据不存在！").put("icon", "warning");
            }
            oldUser.setName(user.getName());
            oldUser.setPwd(user.getPwd());
            userService.update(oldUser);
            return R.ok(204, "成功！").put("user", user).put("icon", "success");
        } catch (Exception e) {
            LOGGER.error("系统内部错误！", e);
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }

    @RequestMapping("{id}/changeRoles")
    public ModelAndView changeRolesPage(ModelAndView mv, @PathVariable("id") Long id){
        mv.setViewName("business/protect/change_roles");
        List<Role> myRoles = userService.findRolesByUserId(id);
        List<Role> allRoles = roleService.findAll();
        mv.addObject("myRoles", myRoles);
        mv.addObject("allRoles", allRoles);
        mv.addObject("userId", id);
        return mv;
    }

    @PutMapping("{id}/saveChange")
    public R saveChange(Long[] roles, @PathVariable("id") Long id) {
        try {
            userService.saveChange(roles, id);
            return R.ok(204, "成功！").put("icon", "success");
        } catch (Exception e) {
            LOGGER.error("系统内部错误！", e);
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }

    @RequestMapping("_")
    public ModelAndView addUserPage(ModelAndView mv) {
        mv.setViewName("business/protect/user");
        return mv;
    }

    @PostMapping("add")
    public R createUser(User user) {
        try {
            if (checkUserName(user)) {
                return R.error(400, "用户名已存在！").put("icon", "warning");
            }
            user.setStatus(User.STATUS_START);
            userService.create(user);
            return R.ok(201, "成功！").put("user", user);
        } catch (Exception e) {
            LOGGER.error("系统内部错误！", e);
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }

    @PatchMapping("{id}/startOrStopUser")
    public R startOrStopUser(@PathVariable("id") Long id) {
        try {
            User user = userService.getById(id);
            if (null == user) {
                return R.error(404, "用户不存在！").put("icon", "warning");
            }
            if (user.getStatus() == User.STATUS_START) {
                user.setStatus(User.STATUS_STOP);
            } else {
                user.setStatus(User.STATUS_START);
            }
            userService.startOrStopUser(user);
            return R.ok(204, "成功！");
        } catch (Exception e) {
            LOGGER.error("系统内部错误！", e);
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }


    private Boolean checkUserName(User user) {
        User oldUser = userService.getByUserName(user.getUserName());
        if (null != oldUser) {
            return true;
        }
        return false;
    }

    private List<User> convertUser(List<User> users) {
        for (User user : users) {
            List<UserRole> userRoles = userService.getUserRoleByUserId(user.getId());
            if (null != userRoles && userRoles.size() > 0) {
                String str = "[";
                for (UserRole userRole : userRoles) {
                    str += userRole.getRoleName() + ",";
                }
                str = str.substring(0, str.length() - 1);
                str += "]";
                user.setRoleName(str);
            }
        }
        return users;
    }
}
