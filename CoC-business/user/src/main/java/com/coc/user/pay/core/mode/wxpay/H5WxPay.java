package com.coc.user.pay.core.mode.wxpay;

import com.coc.user.pay.core.client.WxPayClient;
import com.coc.user.pay.core.constants.PayConstants;
import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.domain.WxPayConfig;
import com.coc.user.pay.core.exception.PayException;
import com.coc.user.pay.core.mode.BaseWxPayMode;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 微信支付 H5支付
 *
 * @author peace
 */
public class H5WxPay extends BaseWxPayMode {

    private final String returnUrl;

    public H5WxPay(PayOrderDTO payOrderDTO, String returnUrl, WxPayConfig wxPayConfig, WxPayClient wxPayClient) {
        super(payOrderDTO, wxPayConfig, wxPayClient);
        this.returnUrl = returnUrl;
    }

    @Override
    public String payment() throws PayException {
        Map<String, String> reqData = getCommonReqData();

        reqData.put("trade_type", "MWEB");
        reqData.put("scene_info", "{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"http://skc-test.iktapp.com\",\"wap_name\": \"商品信息\"}}");

        Map<String, String> map = null;
        try {
            map = getWxPayClient().unifiedOrder(reqData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.notNull(map, "支付信息未收到");
        if (PayConstants.WX_RETURN_MESSAGE_FAILURE.equals(map.get("result_code"))) {
            throw new PayException(map.get("err_code_des"));
        }
        String url = map.get("mweb_url");
        try {
             url = url + "&redirect_url=" + URLEncoder.encode(this.returnUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

}
