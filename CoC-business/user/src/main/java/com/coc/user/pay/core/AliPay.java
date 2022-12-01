package com.coc.user.pay.core;

import com.alipay.api.*;
import com.coc.user.pay.core.domain.AliPayConfig;
import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.mode.*;
import com.coc.user.pay.core.mode.alipay.AppAliPay;
import com.coc.user.pay.core.mode.alipay.H5AliPay;
import com.coc.user.pay.core.mode.alipay.MiniAliPay;
import com.coc.user.pay.core.mode.alipay.PcAliPay;

/**
 * 支付宝支付
 *
 * @author yhf
 */
public class AliPay implements Pay {
    private final AliPayConfig aliPayConfig;

    private AlipayClient alipayClient;

    public AliPay(AliPayConfig aliPayConfig) {
        this.aliPayConfig = aliPayConfig;
        //实例一个支付宝客户端
        setAlipayClient(aliPayConfig);
    }

    @Override
    public BasePayMode getH5Pay(PayOrderDTO payOrderDTO, String returnUrl) {
        return new H5AliPay(payOrderDTO, returnUrl, aliPayConfig, alipayClient);
    }

    @Override
    public BasePayMode getAppPay(PayOrderDTO payOrderDTO) {
        return new AppAliPay(payOrderDTO, aliPayConfig, alipayClient);
    }

    @Override
    public BasePayMode getPcPay(PayOrderDTO payOrderDTO) {
        return new PcAliPay(payOrderDTO, aliPayConfig, alipayClient);
    }

    public BasePayMode getMiniPay(PayOrderDTO payOrderDTO, String code) {
        return new MiniAliPay(payOrderDTO, aliPayConfig, alipayClient, code);
    }

    public void setAlipayClient(AliPayConfig aliPayConfig) {
        AlipayConfig alipayConfig = new AlipayConfig();
        //设置网关地址
        alipayConfig.setServerUrl(aliPayConfig.getGatewayUrl());
        //设置应用Id
        alipayConfig.setAppId(aliPayConfig.getAppId());
        //设置应用私钥
        alipayConfig.setPrivateKey(aliPayConfig.getMerchantPrivateKey());
        //设置请求格式，固定值json
        alipayConfig.setFormat(AlipayConstants.FORMAT_JSON);
        //设置字符集
        alipayConfig.setCharset(AlipayConstants.CHARSET_UTF8);
        //设置支付宝公钥
        alipayConfig.setAlipayPublicKey(aliPayConfig.getAlipayPublicKey());
        //设置签名类型
        alipayConfig.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
        //构造client
        try {
            this.alipayClient = new DefaultAlipayClient(alipayConfig);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }

    public AliPayConfig getAliPayConfigSet() {
        return this.aliPayConfig;
    }

    public AlipayClient getAlipayClient() {
        return this.alipayClient;
    }
}
