package com.eva.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 服务端工具类
 */
@Slf4j
public class ServerUtil {

    /**
     * 获取IP地址
     */
    public String getIP() {
        String ip = "获取失败";
        InetAddress inetAddress = getInetAddress();
        if (inetAddress != null) {
            ip = inetAddress.getHostAddress();
        }
        return ip;
    }

    /**
     * 获取MAC地址
     */
    public String getMAC() {
        try {
            InetAddress inetAddress = getInetAddress();
            if (inetAddress == null) {
                return "获取失败";
            }
            byte[] bs = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bs.length; i++) {
                if (i != 0) {
                    sb.append(":");
                }
                // 字节转换为整数
                int temp = bs[i] & 0xff;
                // 把无符号整数参数所表示的值转换成以十六进制表示的字符串
                String str = Integer.toHexString(temp);
                if (str.length() == 1) {
                    sb.append("0").append(str);
                } else {
                    sb.append(str);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return "获取失败";
        }
    }

    /**
     * 获取InetAddress对象
     */
    private InetAddress getInetAddress () {
        InetAddress inetAddress = null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (name.contains("docker") || name.contains("lo")) {
                    return inetAddress;
                }
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress ia = enumIpAddr.nextElement();
                    if (ia.isLoopbackAddress()) {
                        continue;
                    }
                    String address = ia.getHostAddress();
                    if (address.contains("::") || address.contains("0:0:") || address.contains("fe80")) {
                        continue;
                    }
                    if (!"127.0.0.1".equals(address)) {
                        inetAddress = ia;
                    }
                }
            }
        } catch (SocketException e) {
            log.error(e.getMessage(), e);
        }
        return inetAddress;
    }
}
