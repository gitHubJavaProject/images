package com.img.images.interceptor;


import com.img.images.model.Role;
import com.img.images.model.RoleChecker;
import com.img.images.model.User;
import com.img.images.service.UserService;
import com.img.images.util.FinalKeys;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class UserLoginCheckInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        User user = getLoginUser(request);
        if (user == null) {
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                String distUrl = genRedirectURL(request);
                if (distUrl.contains("business")) {
                    response.sendRedirect("/business/login?from=" + distUrl);
                } else if (distUrl.contains("front")) {
                    response.sendRedirect("/front/login?from=" + distUrl);
                } else {
                    response.sendRedirect("/front/login");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            return false;
        } else {
            if (userService == null) {
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                userService = (UserService) factory.getBean("userService");
            }
            List<Role> roles = userService.findRolesByUserId(user.getId());
            String distUrl = genRedirectURL(request);
            if (!new RoleChecker(roles).hasRole((long)Role.FRONT) && distUrl.contains("front")) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
            if (!new RoleChecker(roles).hasRole((long)Role.BUSINESS) && distUrl.contains("business")) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
            return true;
        }
    }

    protected User getLoginUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(FinalKeys.LOGIN_USER_KEY);
    }

    private String genRedirectURL(HttpServletRequest request) throws UnsupportedEncodingException {
        String distUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
        return URLEncoder.encode(distUrl, "UTF-8");
    }

}
