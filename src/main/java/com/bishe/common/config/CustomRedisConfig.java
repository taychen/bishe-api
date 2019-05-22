package com.bishe.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存配置
 *
 * @author chentai
 * @date 2018/07/22 15:18
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Repository
public class CustomRedisConfig {

    @Resource(name = "stringRedisTemplate")
    public ValueOperations<String, String> valOpsStr;

    public void setAccessToken(String tokenKey, String token, long captchaExpires) {
        valOpsStr.set(tokenKey, token, captchaExpires, TimeUnit.SECONDS);
    }

    public String getAccessToken(String tokenKey) {
        return valOpsStr.get(tokenKey);
    }
}
