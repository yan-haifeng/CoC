package com.coc.user.pay.core.domain;

import lombok.Builder;
import lombok.Data;

/**
 * 加载配置文件
 *
 * @author yhf
 */
@Builder
@Data
public class AliPayConfig {
    /**
     * 应用ID
     */
    private String appId;

    /**
     * 商户PID
     */
    private String sellerId;

    /**
     * 支付宝网关
     */
    private String gatewayUrl;

    /**
     * 商户私钥
     */
    private String merchantPrivateKey;

    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;

    /**
     * 接口内容加密秘钥，对称秘钥
     */
    private String contentKey;

    /**
     * 页面跳转同步通知页面路径
     */
    private String returnUrl;

    /**
     * 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * # 注意：每次重新启动ngrok，都需要根据实际情况修改这个配置
     */
    private String notifyUrl;

    /**
     * 失败返回页面(H5)专用
     */
    private String quitUrl;

    /**
     * 授权异步通知路径
     */
    private String authUrl;

    /**
     * 服务商模式商户的授权令牌
     */
    private String authToken;
}
