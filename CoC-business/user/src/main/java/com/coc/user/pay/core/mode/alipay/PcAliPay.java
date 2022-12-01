package com.coc.user.pay.core.mode.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.coc.user.pay.core.domain.AliPayConfig;
import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.mode.BaseAliPayMode;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 支付宝 网页支付
 *
 * @author peace
 */
public class PcAliPay extends BaseAliPayMode {
    public PcAliPay(PayOrderDTO payOrderDTO, AliPayConfig aliPayConfig, AlipayClient alipayClient) {
        super(payOrderDTO, aliPayConfig, alipayClient);
    }

    @Override
    public String payment() {
        //调用支付宝接口
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        //组装当前业务方法的请求参数
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(getPayOrder().getOutTradeNo());
        model.setTotalAmount(new BigDecimal(getPayOrder().getAmount()).divide(new BigDecimal(100), RoundingMode.UNNECESSARY).doubleValue() + "");
        model.setSubject(getPayOrder().getSubject());
        model.setGoodsDetail(getGoodsDetail());
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        //配置需要的公共请求参数

        //支付完成后，支付宝发起异步通知的地址
        request.setNotifyUrl(getAliPayConfigSet().getNotifyUrl());
        //支付完成后，回跳页面，配置returnUrl
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
