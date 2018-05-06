package com.img.images.controller.front;

import com.img.images.mapper.UserRoleMapper;
import com.img.images.model.Role;
import com.img.images.model.RoleChecker;
import com.img.images.model.User;
import com.img.images.model.UserRole;
import com.img.images.service.UserService;
import com.img.images.util.FinalKeys;
import com.img.images.util.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController("frontAccountController")
@RequestMapping("front")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @RequestMapping("register")
    public ModelAndView register(ModelAndView mv, @RequestParam(value = "from", required = false) String from) {
        mv.setViewName("front/register");
        mv.addObject("from", from);
        return mv;
    }

    @PostMapping("login")
    public Object login(@RequestParam("userName") String username, @RequestParam("pwd") String pwd, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.getByUserName(username);
        if (user == null) {
            return R.error(500, "用户名不正确");
        }
        if (!user.getPwd().equals(pwd.trim())) {
            return R.error(500, "密码不正确");
        }
        List<Role> roles = userService.findRolesByUserId(user.getId());
        if (new RoleChecker(roles).hasRole((long) Role.FRONT)) {
            session.setAttribute(FinalKeys.LOGIN_USER_KEY, user);
            return R.ok(200, "成功");
        } else {
            return R.error(500, "当前用户不能登录！");
        }
    }

    @GetMapping("logout")
    public Object logout(HttpServletRequest request) {
        request.getSession().removeAttribute(FinalKeys.LOGIN_USER_KEY);
        return R.ok(204, "成功");
    }

    @GetMapping("login")
    public ModelAndView login(@RequestParam(value = "from", required = false) String from, ModelAndView mv) {
        mv.setViewName("front/login");
        mv.addObject("from", from);
        return mv;
    }

    @PostMapping("register")
    @Transactional
    public Object registerUser(@RequestParam("name") String name,
                               @RequestParam("pwd") String pwd,
                               @RequestParam("userName") String userName,
                               HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (StringUtils.isBlank(name)) {
            String msg = "昵称不能为空！";
            return R.error(500, msg);
        }
        if (StringUtils.isBlank(pwd)) {
            String msg = "密码不能为空！";
            return R.error(500, msg);
        }
        if (StringUtils.isBlank(userName)) {
            String msg = "用户名不能为空！";
            return R.error(500, msg);
        }
        User existingUser = this.userService.getByUserName(userName);
        if (existingUser != null) {
            String msg = "您的账号已经注册过了";
            return R.error(500, msg);
        }
        User user = new User();
        user.setUserName(userName);
        user.setPwd(pwd);
        user.setName(name);
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId((long)Role.FRONT);
        userRoleMapper.create(userRole);
        session.setAttribute(FinalKeys.LOGIN_USER_KEY, user);
        return R.ok(201, "注册成功！");
    }
}
