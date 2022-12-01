package com.coc.user.pay.core;

import com.coc.user.pay.core.client.WxPayClient;
import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.domain.WxPayConfig;
import com.coc.user.pay.core.mode.*;
import com.coc.user.pay.core.mode.wxpay.*;
import com.github.wxpay.sdk.WXPayConstants;


/**
 * 微信支付
 *
 * @author yhf
 */
public class WeiXPay implements Pay {
    private final WxPayConfig wxPayConfig;

    private WxPayClient wxPayClient;

    public WeiXPay(WxPayConfig wxPayConfig) {
        this.wxPayConfig = wxPayConfig;
        //新建一个微信客户端对象
        setWxPayClient(wxPayConfig);
    }

    @Override
    public BasePayMode getH5Pay(PayOrderDTO payOrderDTO, String returnUrl) {
        return new H5WxPay(payOrderDTO, returnUrl, wxPayConfig, wxPayClient);
    }

    @Override
    public BasePayMode getAppPay(PayOrderDTO payOrderDTO) {
        return new AppWxPay(payOrderDTO, wxPayConfig, wxPayClient);
    }

    @Override
    public BasePayMode getPcPay(PayOrderDTO payOrderDTO) {
        return new PcWxPay(payOrderDTO, wxPayConfig, wxPayClient);
    }

    public BasePayMode getJSApiPay(PayOrderDTO payOrderDTO, String code) {
        return new JSApiWxPay(payOrderDTO, wxPayConfig, wxPayClient, code);
    }

    public BasePayMode getMiniPay(PayOrderDTO payOrderDTO, String code) {
        return new MiniWxPay(payOrderDTO, wxPayConfig, wxPayClient, code);
    }

    public void setWxPayClient(WxPayConfig wxPayConfig) {
        this.wxPayClient = new WxPayClient(wxPayConfig, WXPayConstants.SignType.MD5, false);
    }

    public WxPayClient getWxPayClient() {
        return this.wxPayClient;
    }
}
