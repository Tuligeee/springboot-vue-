package com.mock.example.modules.system.service;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.mock.example.common.consts.Constants;
import com.mock.example.common.utils.ServletUtil;
import com.mock.example.common.utils.ip.AddressUtils;
import com.mock.example.common.utils.ip.IpUtils;
import com.mock.example.modules.system.types.LoginUser;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * token相关业务层 (单机版)
 *
 * @author: Mock
 * @date: 2023-02-26 21:39:32
 */
@Slf4j
@Service
public class TokenService {

    private static final long MILLIS_SECOND = 1000;

    // 令牌自定义标识
    @Value("${token.header}")
    private String header;

    // 令牌秘钥
    @Value("${token.secret}")
    private String secret;

    // 令牌有效期（小时单位）
    @Value("${token.expireTime}")
    private Integer expireTime;

    @Resource(name = "tokenCachePool")
    private Cache<String, LoginUser> tokenCachePool;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);

        if (StrUtil.isNotBlank(token)) {

            Claims claims = parseToken(token);
            // 解析对应的权限以及用户信息
            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);

            String userKey = getTokenKey(uuid);
            LoginUser user = getUserByToken(userKey);

            return user;
        }

        return null;
    }

    /**
     * 删除用户身份信息
     *
     * @param token 用户token
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            getUserByToken(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String token = UUID.randomUUID().toString();
        loginUser.setToken(token);
        setUserAgent(loginUser);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.LOGIN_USER_KEY, token);
        return createToken(claims);
    }

    /**
     * 设置用户代理信息
     *
     * @param loginUser 登录信息
     */
    public void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtil.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtil.getRequest());
        loginUser.setIpaddr(ip);
        loginUser.setLoginLocation(AddressUtils.getRealAddressByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 验证令牌有效期，相差不足24小时，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= getTokenExpireTimeMillis()) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() +
                expireTime * getTokenExpireTimeMillis());
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        setUserByToken(userKey, loginUser);
    }

    /**
     * 获取请求token
     *
     * @param request 请求对象
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(Constants.TOKEN_PREFIX)) {
            token = token.replace(Constants.TOKEN_PREFIX, "");
        }
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取缓存中的用户信息
     *
     * @param token 用户token
     * @return 用户登录信息
     */
    public LoginUser getUserByToken(String token) {
        return tokenCachePool.getIfPresent(token);
    }

    /**
     * 用户信息放入token缓存
     *
     * @param token     用户token
     * @param loginUser 用户信息
     */
    public void setUserByToken(String token, LoginUser loginUser) {
        tokenCachePool.put(token, loginUser);
    }

    /**
     * 删除用户token
     *
     * @param token 用户token
     */
    public void removeUserByToken(String token) {
        tokenCachePool.invalidate(token);
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (loginUser != null && StrUtil.isNotBlank(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    private String getTokenKey(String uuid) {
        return Constants.LOGIN_TOKEN_KEY + uuid;
    }

    public Long getTokenExpireTimeMillis() {
        return expireTime * 60 * MILLIS_SECOND;
    }

}

  