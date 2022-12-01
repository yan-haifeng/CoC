package com.coc.user.pay.core.mode.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.coc.user.pay.core.domain.AliPayConfig;
import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.exception.PayException;
import com.coc.user.pay.core.mode.BaseAliPayMode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 支付宝 小程序支付
 *
 * @author peace
 */
public class MiniAliPay extends BaseAliPayMode {
    private final String code;

    public MiniAliPay(PayOrderDTO payOrderDTO, AliPayConfig aliPayConfig, AlipayClient alipayClient, String code) {
        super(payOrderDTO, aliPayConfig, alipayClient);
        this.code = code;
    }

    @Override
    public String payment() throws PayException {
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();

        AlipayTradeCreateModel model = new AlipayTradeCreateModel();
        model.setOutTradeNo(getPayOrder().getOutTradeNo());
        model.setTotalAmount(new BigDecimal(getPayOrder().getAmount()).divide(new BigDecimal(100), RoundingMode.UNNECESSARY).doubleValue() + "");
        model.setSubject(getPayOrder().getSubject());
        model.setProductCode("QUICK_WAP_WAY");
        model.setGoodsDetail(getGoodsDetail());
        model.setBuyerId(getUserId(code));

        request.setNotifyUrl(getAliPayConfigSet().getNotifyUrl());
        request.setReturnUrl(getAliPayConfigSet().getReturnUrl());
        //服务商模式加入用户授权
        if (serviceProvider) {
            String authToken = getAliPayConfigSet().getAuthToken();
            if (authToken != null && !"".equals(authToken)) {
                request.putOtherTextParam("app_auth_token", authToken);
            }
        }
        request.setBizModel(model);
        try {
            return getAlipayClient().execute(request).getTradeNo();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getUserId(String code) {
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(code);
        //服务商模式加入用户授权
        if (serviceProvider) {
            String authToken = getAliPayConfigSet().getAuthToken();
            if (authToken != null && !"".equals(authToken)) {
                request.putOtherTextParam("app_auth_token", authToken);
            }
        }
        try {
            return getAlipayClient().execute(request).getUserId();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}
