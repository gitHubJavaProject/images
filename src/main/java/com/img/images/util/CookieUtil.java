package com.img.images.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class CookieUtil {
    public final static int AGE_TILL_BROWSER_EXIT = -1;
    public final static int AGE_REMOVED = 0;
    public final static int AGE_DEFAULT_MAX = (int) TimeUnit.DAYS.toSeconds(365);
    public final static int AGE_WEEK = (int) TimeUnit.DAYS.toSeconds(7);
    public final static String DEFAULT_PATH = "/";

    private CookieUtil() {
    }

    public static void setCookie(HttpServletResponse response, String key, String value) {
        setCookie(response, key, value, AGE_DEFAULT_MAX);
    }

    public static void setCookie(HttpServletResponse response, String key, String value, int maxAge) {
        setCookie(response, key, value, DEFAULT_PATH, maxAge);
    }

    public static void setCookie(HttpServletResponse response, String key, String value, String path, int maxAge) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void removeCookie(HttpServletResponse response, String key) {
        removeCookie(response, key, DEFAULT_PATH);
    }

    public static void removeCookie(HttpServletResponse response, String key, String path) {
        setCookie(response, key, null, path, AGE_REMOVED);
    }
}  