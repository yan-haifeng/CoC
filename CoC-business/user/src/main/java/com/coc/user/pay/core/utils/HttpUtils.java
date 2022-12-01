package com.coc.user.pay.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * 支付http工具
 *
 * @author peace
 */
@Component
@Slf4j
public class HttpUtils {

    private final static String UNKNOW = "unknow";
    private final static String LOCAL_IP = "127.0.0.1";

    /**
     * 将通知参数转化为字符串
     *
     * @param request 通知参数
     * @return 字符串格式参数
     */
    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder result = new StringBuilder();
            br = request.getReader();
            for (String line; (line = br.readLine()) != null; ) {
                if (result.length() > 0) {
                    result.append("\n");
                }
                result.append(line);
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error(String.valueOf(e));
                }
            }
        }
    }

    /**
     * 获取本地url，使支付宝成功后能跳回支付成功页面
     *
     * @return 返回本地url及端口号
     */
    public static String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "https://" + Objects.requireNonNull(address).getHostAddress();
    }

    /**
     * 生成16位不重复的随机数，含数字+大小写
     */
    public static String getGUID() {
        int guidLength = 16;
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < guidLength; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type) {
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char) (rd.nextInt(25) + 65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char) (rd.nextInt(25) + 97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    /**
     * 支付宝返回参数处理
     *
     * @return 返回key value格式参数
     */
    public static Map<String, String> getParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>(16);
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String o : requestParams.keySet()) {
            String[] values = requestParams.get(o);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            params.put(o, valueStr);
        }
        return params;
    }

    /**
     * 获取客户端IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOW.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOW.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOW.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (LOCAL_IP.equals(ip)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Assert.notNull(inet, "网卡获取失败");
                ip = inet.getHostAddress();
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        int ipMaxLength = 15;
        // 分割标志
        String splitMark = ",";
        if (ip != null && ip.length() > ipMaxLength) {
            if (ip.indexOf(splitMark) > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }
}