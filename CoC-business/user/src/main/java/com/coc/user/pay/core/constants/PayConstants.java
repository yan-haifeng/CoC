package com.coc.user.pay.core.constants;

/**
 * 支付常量
 * @author yhf
 */
public class PayConstants {

    /**
     * 支付方式 支付宝支付
     */
    public static final int PAY_MODE_ALIPAY = 1;

    /**
     * 支付方式 微信支付
     */
    public static final int PAY_MODE_WXPAY = 2;


    /**
     * 支付网关 支付宝沙箱网关
     */
    public static final String ALI_GATEWAY_URL_SENDBOX = "https://openapi.alipaydev.com/gateway.do";

    /**
     * 支付网关 支付宝网关
     */
    public static final String ALI_GATEWAY_URL = "https://openapi.alipay.com/gateway.do";

    /**
     * 收到异步消息后返回给第三方的消息
     * 支付宝
     * 成功
     */
    public static final String ALI_RETURN_MESSAGE_SUCCESS = "success";

    /**
     * 收到异步消息后返回给第三方的消息
     * 支付宝
     * 失败
     */
    public static final String ALI_RETURN_MESSAGE_FAILURE = "failure";

    /**
     * 收到异步消息后返回给第三方的消息
     * 微信
     * 成功
     */
    public static final String WX_RETURN_MESSAGE_SUCCESS = "SUCCESS";

    /**
     * 收到异步消息后返回给第三方的消息
     * 微信
     * 失败
     */
    public static final String WX_RETURN_MESSAGE_FAILURE = "FAIL";

    /**
     * 微信jsapi获取openId的链接
     */
    public static final String WX_OPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 微信小程序获取openId的链接
     */
    public static final String WX_MINI_OPENID_URL = "https://api.weixin.qq.com/sns/jscode2session";
}
