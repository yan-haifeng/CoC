package com.coc.user.pay.core.mode.wxpay;

import com.alibaba.fastjson.JSONObject;
import com.coc.user.pay.core.client.WxPayClient;
import com.coc.user.pay.core.constants.PayConstants;
import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.domain.WxPayConfig;
import com.coc.user.pay.core.mode.BaseWxPayMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付 JSApi支付
 *
 * @author peace
 */
public class JSApiWxPay extends BaseWxPayMode {
    private final String code;

    public JSApiWxPay(PayOrderDTO payOrderDTO, WxPayConfig wxPayConfig, WxPayClient wxPayClient, String code) {
        super(payOrderDTO, wxPayConfig, wxPayClient);
        this.code = code;
    }

    @Override
    public String payment() {
        Map<String, String> reqData = getCommonReqData();
        // 交易类型 小程序取值
        reqData.put("trade_type", "JSAPI");
        // APPID
        if (serviceProvider) {
            reqData.put("sub_appid", getWxPayConfigSet().getSubAppid());
        }
        // trade_type=JSAPI，此参数必传，用户在子商户appid下的唯一标识。
        // openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。
        try {
            reqData.put("openid", this.getOpenId());
            return getPaySign(getWxPayClient().unifiedOrder(reqData));
        } catch (Exception e) {
            e.printStackTrace();
            return "code无效";
        }
    }

    public String getOpenId() throws Exception {
        String url = String.format(PayConstants.WX_OPENID_URL + "?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                getWxPayConfigSet().getAppID(), getWxPayConfigSet().getSecret(), this.code);
        JSONObject jsonObject = JSONObject.parseObject(getWxPayClient().requestWithoutCert(url, new HashMap<>(16), 8000, 10000));
        if (jsonObject.getString("errcode") == null) {
            return jsonObject.getString("openid");
        } else {
            throw new Exception(jsonObject.getString("errmsg"));
        }
    }

    /**
     * 获取code
     */
    public String getCode() {
        // TODO: 2022/11/23 这个链接要改为自己的 
        String redirectUri = "https://8x97nw.natappfree.cc/pay/wxCodeNotify";
        String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=123#wechat_redirect",
                getWxPayConfigSet().getAppID(), redirectUri);
        return "redirect:" + url;
    }
}
