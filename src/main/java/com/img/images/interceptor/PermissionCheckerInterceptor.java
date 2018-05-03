package com.img.images.interceptor;


import com.img.images.model.*;
import com.img.images.service.UserService;
import com.img.images.util.FinalKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

public class
PermissionCheckerInterceptor extends HandlerInterceptorAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionCheckerInterceptor.class);

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User loginUser = (User) request.getSession().getAttribute(FinalKeys.LOGIN_USER_KEY);
        if (loginUser == null) {
            LOGGER.debug("permission check failed for not login.");
            return true;
        }

        if (userService == null) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            userService = (UserService) factory.getBean("userService");
        }
        if (request.getAttribute(FinalKeys.ROLE_CHECKER) == null) {
            List<Role> roles = userService.findRolesByUserId(loginUser.getId());
            RoleChecker roleChecker = new RoleChecker(roles);
            request.setAttribute(FinalKeys.ROLE_CHECKER, roleChecker);
        }
        return checkPermission(request, response, handler);
    }
    private boolean checkPermission(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler) throws IOException {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        boolean hasPermission = true;
        List<Privilege> privileges = userService.getPrivilegesByUser(getLoginUser(request).getId());
        String distUrl = genRedirectURL(request);
        distUrl = URLDecoder.decode(distUrl, "utf-8");
        if (!distUrl.endsWith("/")) {
            distUrl += "/";
        }
        if (!distUrl.startsWith("/")) {
            distUrl = "/" + distUrl;
        }
        hasPermission = new PrivilegeChecker(privileges).hasPrivilege(distUrl);
        if (hasPermission) {
            request.setAttribute(FinalKeys.PERMISSION_CHECKER, new PrivilegeChecker(privileges));
            LOGGER.info("user {} has permission for expression {}.", ((User) request.getAttribute(FinalKeys.LOGIN_USER_KEY)).getId(), "");
            return true;
        }
        LOGGER.info("send error 403");
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        return false;
    }

    protected User getLoginUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(FinalKeys.LOGIN_USER_KEY);
    }

    private String genRedirectURL(HttpServletRequest request) throws UnsupportedEncodingException {
        String distUrl = request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI();
        return URLEncoder.encode(distUrl, "UTF-8");
    }
}
