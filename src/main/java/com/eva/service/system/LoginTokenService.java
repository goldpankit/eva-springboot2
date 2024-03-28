package com.eva.service.system;

import com.eva.core.constants.Constants;
import com.eva.core.exception.InvalidTokenException;
import com.eva.core.model.LoginUserInfo;
import com.eva.service.common.CacheProxy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 登录令牌
 */
@Service
public class LoginTokenService {

    @Resource
    private CacheProxy<String, LoginUserInfo> cacheProxy;

    /**
     * 生成令牌
     *
     * @return 令牌
     */
    public String generate () {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取令牌
     *
     * @param request HttpServletRequest
     * @return 令牌
     */
    public String get (HttpServletRequest request) {
        // 从cookie中获取认证
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Constants.HEADER_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        // 从header中获取认证
        return request.getHeader(Constants.HEADER_TOKEN);
    }

    /**
     * 删除令牌
     *
     * @param request HttpServletRequest
     */
    public void remove (HttpServletRequest request) {
        String token = this.get(request);
        if (token != null) {
            cacheProxy.remove(token);
        }
    }

    /**
     * 验证令牌
     *
     * @param token 令牌
     * @throws InvalidTokenException InvalidTokenException
     */
    public void check(String token) throws InvalidTokenException {
        if (token == null || token.length() != 36) {
            throw new InvalidTokenException();
        }
    }
}
