package com.bishe.common.security.interceptor;

import com.bishe.common.exception.ServerException;
import com.bishe.common.exception.TokenException;
import com.bishe.common.properties.ResultEnums;
import com.bishe.common.security.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 安全校验拦截器
 *
 * @author chentay
 * @date 2019/01/16
 */
@Slf4j
public class SecurityInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //  配置对404的拦截
        int status = response.getStatus();
        if (status == ResultEnums.URL_NOT_FOUND.getCode()) {
            throw new ServerException(ResultEnums.URL_NOT_FOUND);
        }
        //  配置对500的拦截
        if (status == ResultEnums.SERVER_EXCEPTION.getCode()) {
            throw new ServerException(ResultEnums.SERVER_EXCEPTION);
        }

        String accessToken = request.getHeader("Authorization");
        log.info("【SecurityInterceptor】preHandle accessToken:{}", accessToken);
        String bearer = "bearer ";
        // 判断是否传入token
        if (StringUtils.isEmpty(accessToken)) {
            throw new TokenException(ResultEnums.ACCESS_DENIED);
        } else {
            accessToken = accessToken.substring(0, 1).toLowerCase() + accessToken.substring(1);
            if (!accessToken.startsWith(bearer)) {
                throw new TokenException(ResultEnums.ACCESS_DENIED);
            } else {
                String[] accessTokens = accessToken.split(" ");
                accessToken = accessTokens[1];
                if (StringUtils.isEmpty(accessToken)) {
                    throw new TokenException(ResultEnums.ACCESS_DENIED);
                }
            }
        }
        //  判断token是否有效
        Claims claims = JwtUtils.getInstance().parseJWT(accessToken);
        log.info("【SecurityInterceptor】preHandle token:{}", accessToken);
        if (claims != null) {
            return true;
        } else {
            throw new TokenException(ResultEnums.ACCESS_DENIED);
        }
    }
}
