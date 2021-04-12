package com.aeon.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class TokenUtils {

    public static String parseJwt(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
