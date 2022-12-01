package com.coc.user.pay.core.domain;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

/**
 * 微信支付配置对象
 *
 * @author peace
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxPayConfig implements WXPayConfig {

    /**
     * 公众账号ID
     */
    private String appID;

    /**
     * 商户号
     */
    private String mchID;

    /**
     * secret
     */
    private String secret;

    /**
     * API 密钥
     */
    private String key;

    /**
     * API证书绝对路径
     */
    private String certPath;

    /**
     * 支付成功异步通知地址
     */
    private String payNotifyUrl;

    /**
     * 退款异步通知地址
     */
    private String refundNotifyUrl;

    /**
     * HTTP(S) 连接超时时间，单位毫秒
     */
    private int httpConnectTimeoutMs;

    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     */
    private int httpReadTimeoutMs;

    /**
     * 子商户Openid
     */
    private String subOpenid;

    /**
     * 子商户Appid
     */
    private String subAppid;

    /**
     * 子商户MchId
     */
    private String subMchId;

    /**
     * 子商户 密钥
     */
    private String subKey;

    @Override
    public InputStream getCertStream() {
        InputStream inputStream = null;
        try {
            URL url = this.getClass().getClassLoader().getResource("");
            Assert.notNull(url, "文件url为空");
            File file = ResourceUtils.getFile(url.getPath() + certPath);
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
