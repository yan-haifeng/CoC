package com.coc.user.pay.core.mode;

import com.alibaba.fastjson.JSONObject;
import com.coc.user.pay.core.client.WxPayClient;
import com.coc.user.pay.core.constants.PayConstants;
import com.coc.user.pay.core.domain.PayGoods;
import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.domain.WxPayConfig;
import com.coc.user.pay.core.exception.PayException;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 支付宝支付公共方法
 *
 * @author yhf
 */
public abstract class BaseWxPayMode extends BasePayMode {

    /**
     * 设置微信配置参数
     */
    private final WxPayConfig wxPayConfig;

    private final WxPayClient wxPayClient;

    public BaseWxPayMode(PayOrderDTO payOrderDTO, WxPayConfig wxPayConfig, WxPayClient wxPayClient) {
        super(payOrderDTO);
        this.wxPayConfig = wxPayConfig;
        this.wxPayClient = wxPayClient;
    }

    public WxPayConfig getWxPayConfigSet() {
        return this.wxPayConfig;
    }

    public WxPayClient getWxPayClient() {
        return this.wxPayClient;
    }

    @Override
    public Map<String, String> tradeQuery() {
        Map<String, String> reqData = new HashMap<>(16);
        reqData.put("sub_mch_id", getWxPayConfigSet().getSubMchId());
        reqData.put("out_trade_no", getPayOrder().getOutTradeNo());
        try {
            return wxPayClient.orderQuery(reqData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> refund() throws PayException {
        Map<String, String> reqData = this.getCommonReqData();
        reqData.put("out_refund_no", UUID.randomUUID().toString());
        reqData.put("refund_fee", getPayOrder().getAmount());
        reqData.put("notify_url", getWxPayConfigSet().getRefundNotifyUrl());
        // 不需要的参数移除
        reqData.remove("body");
        reqData.remove("spbill_create_ip");
        reqData.remove("detail");
        Map<String, String> map = null;
        try {
            map = getWxPayClient().refund(reqData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.notNull(map, "查询失败");
        if (PayConstants.WX_RETURN_MESSAGE_FAILURE.equals(map.get("err_code_des"))) {
            throw new PayException(map.get("err_code_des"));
        }
        map = this.refundQuery();
        return map;
    }

    public Map<String, String> refundQuery() throws PayException {
        Map<String, String> reqData = new HashMap<>(16);
        reqData.put("out_trade_no", getPayOrder().getOutTradeNo());
        if (serviceProvider) {
            //子商户号
            reqData.put("sub_mch_id", getWxPayConfigSet().getSubMchId());
        }
        Map<String, String> map = null;
        try {
            map = getWxPayClient().refundQuery(reqData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Assert.notNull(map, "查询失败");
        if (PayConstants.WX_RETURN_MESSAGE_FAILURE.equals(map.get("result_code"))) {
            throw new PayException(map.get("err_code_des"));
        }
        return map;
    }

    /**
     * jsapi返回的预支付交易会话标识需要二次签名调用此方法
     *
     * @param wxResult 微信端发来的信息
     * @return 二次签名后的预支付交易会话标识
     */
    public String getPaySign(Map<String, String> wxResult) throws Exception {
        if (PayConstants.WX_RETURN_MESSAGE_FAILURE.equals(wxResult.get("result_code"))) {
            throw new PayException(wxResult.get("err_code_des"));
        }
        HashMap<String, String> data = new HashMap<>(16);
        data.put("appId", wxPayConfig.getSubAppid());
        data.put("nonceStr", wxResult.get("nonce_str"));
        data.put("package", "prepay_id=" + wxResult.get("prepay_id"));
        data.put("signType", "MD5");
        data.put("timeStamp", "" + System.currentTimeMillis() / 1000);
        data.put("paySign", WXPayUtil.generateSignature(data, wxPayConfig.getSubKey(), WXPayConstants.SignType.MD5));
        return JSONObject.toJSON(data).toString();
    }

    public Map<String, String> getCommonReqData() {
        Map<String, String> reqData = new HashMap<>(16);
        reqData.put("total_fee", getPayOrder().getAmount());
        // 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        reqData.put("out_trade_no", getPayOrder().getOutTradeNo());
        reqData.put("body", getPayOrder().getBody());
        // APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
        reqData.put("spbill_create_ip", getPayOrder().getPayerClientIp());
        reqData.put("notify_url", getWxPayConfigSet().getPayNotifyUrl());
        reqData.put("detail", getGoodsDetail());
        // 自定义参数, 可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
        reqData.put("device_info", "WEB");
        if (serviceProvider) {
            //子商户号
            reqData.put("sub_mch_id", getWxPayConfigSet().getSubMchId());
        }
        return reqData;
    }

    public String getGoodsDetail() {
        List<PayGoods> goodsDetail = getPayOrder().getGoodsDetail();
        Map<String, Object> map = new HashMap<>(16);
        map.put("goods_detail", goodsDetail);
        List<Object> list = new ArrayList<>();
        list.add(map);
        return list.toString();
    }

    public BasePayMode useServiceProvider() {
        this.serviceProvider = true;
        return this;
    }

    public BasePayMode closeServiceProvider() {
        this.serviceProvider = false;
        return this;
    }
}
