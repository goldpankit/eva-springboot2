package com.eva.core.prevent;

import com.eva.core.constants.Constants;
import com.eva.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.TreeMap;

/**
 * 默认防重复调用实现
 * 实现逻辑：从请求中获取请求路径、登录令牌和客户端IP作为请求因子，并将因子转为MD5作为请求标识
 */
@Slf4j
@Component
public class PreventRepeatDefaultHandler extends PreventRepeatHandlerAdapter {

    @Override
    public String sign(HttpServletRequest request) {
        // 获取参数
        Map<String, Object> parameters = new TreeMap<>();
        parameters.put("REQUEST_URI", request.getRequestURI());
        parameters.put("USER_TOKEN", String.valueOf(request.getHeader(Constants.HEADER_TOKEN)));
        parameters.put("IP", Utils.User_Client.getIP(request));
        // 构建参数签名字符串
        StringBuilder signString = new StringBuilder();
        for(String key : parameters.keySet()) {
            signString
                    .append(key)
                    .append("=")
                    .append(parameters.get(key))
                    .append(";");
        }
        // 参数签名
        return DigestUtils.md5DigestAsHex(signString.toString().getBytes());
    }
}
