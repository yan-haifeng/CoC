package com.coc.user.pay.core.client;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付客户端
 * @author yhf
 */
public class WxPayClient extends WXPay {

    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";

    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS5Padding";

    /**
     * 微信支付配置
     */
    private final WXPayConfig config;

    public WxPayClient(WXPayConfig config, WXPayConstants.SignType signType, boolean useSandbox) {
        super(config, signType, useSandbox);
        this.config = config;
    }

    /**
     * 从request的inputStream中获取参数
     */
    public Map<String, String> getNotifyParameter(HttpServletRequest request) throws Exception {
        InputStream inputStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, length);
        }
        outSteam.close();
        inputStream.close();

        // 获取微信调用我们notify_url的返回信息
        String resultXml = outSteam.toString("utf-8");
        return WXPayUtil.xmlToMap(resultXml);
    }

    /**
     * 解密退款通知
     * <a href="https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=9_16&index=11">退款结果通知文档</a>
     */
    @SuppressWarnings("restriction")
    public Map<String, String> decodeRefundNotify(HttpServletRequest request) throws Exception {
        // 从request的流中获取参数
        Map<String, String> notifyMap = this.getNotifyParameter(request);

        String reqInfo = notifyMap.get("req_info");
        //（1）对加密串A做base64解码，得到加密串B
        byte[] bytes = new BASE64Decoder().decodeBuffer(reqInfo);

        //（2）对商户key做md5，得到32位小写key* ( key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置 )
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING);
        SecretKeySpec key = new SecretKeySpec(WXPayUtil.MD5(config.getKey()).toLowerCase().getBytes(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        //（3）用key*对加密串B做AES-256-ECB解密（PKCS7Padding）
        // java.security.InvalidKeyException: Illegal key size or default parameters
        // https://www.cnblogs.com/yaks/p/5608358.html
        String responseXml = new String(cipher.doFinal(bytes), StandardCharsets.UTF_8);
        return WXPayUtil.xmlToMap(responseXml);
    }

    /**
     * 获取沙箱环境验签秘钥API
     * <a href="https://pay.weixin.qq.com/wiki/doc/api/micropay.php?chapter=23_1">获取验签秘钥API文档</a>
     */
    public Map<String, String> getSignKey() throws Exception {
        Map<String, String> reqData = new HashMap<>(16);
        reqData.put("mch_id", config.getMchID());
        reqData.put("nonce_str", WXPayUtil.generateNonceStr());
        String sign = WXPayUtil.generateSignature(reqData, config.getKey(), WXPayConstants.SignType.MD5);
        reqData.put("sign", sign);
        String responseXml = this.requestWithoutCert("https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey", reqData,
                config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs());

        Map<String, String> responseMap = WXPayUtil.xmlToMap(responseXml);
        System.out.println("密钥！！！");
        return responseMap;
    }
}
