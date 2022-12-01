package com.coc.user.pay.core.mode.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.coc.user.pay.core.domain.AliPayConfig;
import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.mode.BaseAliPayMode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 支付宝 H5支付
 *
 * @author peace
 */
public class H5AliPay extends BaseAliPayMode {

    public H5AliPay(PayOrderDTO payOrderDTO, String returnUrl, AliPayConfig aliPayConfig, AlipayClient alipayClient) {
        super(payOrderDTO, aliPayConfig, alipayClient);
        getAliPayConfigSet().setReturnUrl(returnUrl);
    }

    @Override
    public String payment() {
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(getPayOrder().getOutTradeNo());
        model.setTotalAmount(new BigDecimal(getPayOrder().getAmount()).divide(new BigDecimal(100), RoundingMode.UNNECESSARY).doubleValue() + "");
        model.setSubject(getPayOrder().getSubject());
        model.setProductCode("QUICK_WAP_WAY");
        model.setQuitUrl(getAliPayConfigSet().getQuitUrl());
        model.setGoodsDetail(getGoodsDetail());

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
            return getAlipayClient().pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return null;
    }
}
