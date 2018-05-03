package com.img.images.controller.business;

import com.img.images.model.Role;
import com.img.images.model.RoleChecker;
import com.img.images.model.User;
import com.img.images.service.RoleService;
import com.img.images.service.UserService;
import com.img.images.util.FinalKeys;
import com.img.images.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("business")
public class AccountController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("login")
    public ModelAndView login(@RequestParam(value = "from", required = false) String from, ModelAndView mv) {
        mv.setViewName("business/login");
        mv.addObject("from", from);
        return mv;
    }

    @PostMapping("login")
    @ResponseBody
    public Object login(@RequestParam("username") String username, @RequestParam("pwd") String pwd, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.getByUserName(username);
        if (user == null) {
            return R.error(500, "用户名不正确");
        }
        if (!user.getPwd().equals(pwd.trim())) {
            return R.error(500, "密码不正确");
        }
        List<Role> roles = userService.findRolesByUserId(user.getId());
        if (new RoleChecker(roles).hasRole((long)Role.BUSINESS)) {
            session.setAttribute(FinalKeys.LOGIN_USER_KEY, user);
            return R.ok(200, "成功");
        } else {
            return R.error(500, "当前用户不能登录！");
        }
    }

    @GetMapping("logout")
    public Object logout(HttpServletRequest request) {
        request.getSession().removeAttribute(FinalKeys.LOGIN_USER_KEY);
        return new ModelAndView(new RedirectView("/business/login"));
    }

    @GetMapping("p/reset_pwd")
    public ModelAndView resetPwd() {
        return new ModelAndView("business/protect/reset_pwd");
    }

}
