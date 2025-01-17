package com.eva.core.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求工具类
 */
public class UserClientUtil {

    /**
     * 获取系统信息
     *
     * @param request 请求对象
     * @return String
     */
    public String getOS(HttpServletRequest request) {
        String browserDetails = request.getHeader("User-Agent");
        String userAgent = browserDetails.toLowerCase();
        String os;
        if (userAgent.contains("windows")) {
            os = "Windows";
        } else if (userAgent.contains("mac")) {
            os = "Mac";
        } else if (userAgent.contains("x11")) {
            os = "Unix";
        } else if (userAgent.contains("android")) {
            os = "Android";
        } else if (userAgent.contains("iphone")) {
            os = "IPhone";
        } else {
            os = "UnKnown, More-Info: " + userAgent;
        }
        return os;

    }

    /**
     * 获取客户端信息
     *
     * @param request 请求对象
     * @return String
     */
    public String getBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String user = userAgent.toLowerCase();

        String browser = "";

        if (user.contains("edge")) {
            browser = (userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("msie")) {
            String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version")) {
            browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
                    + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if (user.contains("opr") || user.contains("opera")) {
            if (user.contains("opera")) {
                browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
                        + (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            } else if (user.contains("opr")) {
                browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
                        .replace("OPR", "Opera");
            }

        } else if (user.contains("chrome")) {
            browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.contains("mozilla/7.0")) || (user.contains("netscape6"))
                || (user.contains("mozilla/4.7")) || (user.contains("mozilla/4.78"))
                || (user.contains("mozilla/4.08")) || (user.contains("mozilla/3"))) {
            browser = "Netscape-?";

        } else if (user.contains("firefox")) {
            browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("rv")) {
            String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
            browser = "IE" + IEVersion.substring(0, IEVersion.length() - 1);
        } else {
            browser = "UnKnown, More-Info: " + userAgent;
        }

        return browser;
    }

    /**
     * 获取IP
     *
     * @param request 请求对象
     * @return String
     */
    public String getIP(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        if(!StringUtils.isBlank(ip)) {
            if(ip.contains(",")) {
                return ip.split(",")[0].trim();
            } else {
                return ip.trim();
            }
        }
        return "-1.-1.-1.-1";
    }

    /**
     * 获取用户操作平台
     *
     * @param request 请求对象
     * @return String
     */
    public String getPlatform (HttpServletRequest request) {
        return request.getHeader("eva-platform") == null ? "UNKNOWN" : request.getHeader("eva-platform");
    }
}
