package com.coc.user.pay.core.mode.wxpay;

import com.alibaba.fastjson.JSONObject;
import com.coc.user.pay.core.client.WxPayClient;
import com.coc.user.pay.core.constants.PayConstants;
import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.domain.WxPayConfig;
import com.coc.user.pay.core.exception.PayException;
import com.coc.user.pay.core.mode.BaseWxPayMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信支付 小程序支付
 *
 * @author peace
 */
public class MiniWxPay extends BaseWxPayMode {
    private final String code;

    public MiniWxPay(PayOrderDTO payOrderDTO, WxPayConfig wxPayConfig, WxPayClient wxPayClient, String code) {
        super(payOrderDTO, wxPayConfig, wxPayClient);
        this.code = code;
    }

    @Override
    public String payment() throws PayException {
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
            reqData.put("openid", this.getMiniOpenId());
            return getPaySign(getWxPayClient().unifiedOrder(reqData));
        } catch (Exception e) {
            e.printStackTrace();
            return "code无效";
        }
    }

    /**
     * 获取小程序登录用户的openId
     */
    public String getMiniOpenId() throws Exception {
        String url = String.format(PayConstants.WX_MINI_OPENID_URL + "?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                getWxPayConfigSet().getAppID(), getWxPayConfigSet().getSecret(), this.code);
        JSONObject jsonObject = JSONObject.parseObject(getWxPayClient().requestWithoutCert(url, new HashMap<>(16), 8000, 10000));
        if ("0".equals(jsonObject.getString("errcode"))) {
            return jsonObject.getString("openid");
        } else {
            throw new PayException(jsonObject.getString("errmsg"));
        }
    }
}
