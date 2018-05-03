package com.img.images.controller.front;

import com.img.images.model.Role;
import com.img.images.model.RoleChecker;
import com.img.images.model.User;
import com.img.images.service.UserService;
import com.img.images.util.FinalKeys;
import com.img.images.util.R;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping("register")
    public ModelAndView register(ModelAndView mv) {
        mv.setViewName("front/register");
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
        if (new RoleChecker(roles).hasRole((long)Role.FRONT)) {
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

}
