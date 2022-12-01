package com.coc.user.pay.core.mode.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.coc.user.pay.core.domain.AliPayConfig;
import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.mode.BaseAliPayMode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 支付宝 App支付
 *
 * @author peace
 */
public class AppAliPay extends BaseAliPayMode {

    public AppAliPay(PayOrderDTO payOrderDTO, AliPayConfig aliPayConfig, AlipayClient alipayClient) {
        super(payOrderDTO, aliPayConfig, alipayClient);
    }

    @Override
    public String payment() {
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(getPayOrder().getOutTradeNo());
        model.setSubject(getPayOrder().getSubject());
        model.setTotalAmount(new BigDecimal(getPayOrder().getAmount()).divide(new BigDecimal(100), RoundingMode.UNNECESSARY).doubleValue() + "");
        model.setGoodsDetail(getGoodsDetail());
        model.setProductCode("QUICK_MSECURITY_PAY");

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
            return getAlipayClient().sdkExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return null;
    }
}
