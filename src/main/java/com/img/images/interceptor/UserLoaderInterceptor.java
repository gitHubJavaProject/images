package com.img.images.interceptor;


import com.img.images.model.User;
import com.img.images.util.FinalKeys;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoaderInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute(FinalKeys.LOGIN_USER_KEY);

        if (user != null) {
            request.setAttribute(FinalKeys.LOGIN_USER_KEY, user);
        }

        return super.preHandle(request, response, handler);
    }
}
