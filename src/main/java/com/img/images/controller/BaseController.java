package com.img.images.controller;

import com.img.images.model.User;
import com.img.images.service.UserService;
import com.img.images.util.FinalKeys;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserService userService;

    protected User getLoginUser() {
        return (User) this.request.getSession().getAttribute(FinalKeys.LOGIN_USER_KEY);
    }

}
