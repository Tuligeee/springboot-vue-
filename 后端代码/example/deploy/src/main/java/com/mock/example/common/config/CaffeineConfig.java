package com.mock.example.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.mock.example.modules.system.entity.model.SysDictData;
import com.mock.example.modules.system.types.LoginUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine 缓存初始化
 *
 * @author: Mock
 * @date: 2023-02-26 22:07:11
 */
@Configuration
public class CaffeineConfig {

    @Value("${token.expireTime}")
    private Long tokenExpire;

    /**
     * 登录用户信息token缓存池
     * 会定期清池里token
     */
    @Bean("tokenCachePool")
    public Cache<String, LoginUser> tokenCache() {
        // 设置超时时间 按小时为单位
        return Caffeine.newBuilder()
                .expireAfterWrite(tokenExpire, TimeUnit.HOURS)
                .build();
    }

    /**
     * 字典缓存
     */
    @Bean("dictCachePool")
    public Cache<String, List<SysDictData>> dictCache() {
        // 不设置缓存过期时间
        return Caffeine.newBuilder()
                .build();
    }

    /**
     * 可重复提交缓存
     */
    @Bean("repeatSubmitCache")
    public Cache<String, Object> repeatSubmitCache() {
        // 设置超时时间为10秒，超过该时间缓存过期
        final long tokenExpire = 10;
        return Caffeine.newBuilder()
                .expireAfterWrite(tokenExpire, TimeUnit.SECONDS)
                .build();
    }

}

  