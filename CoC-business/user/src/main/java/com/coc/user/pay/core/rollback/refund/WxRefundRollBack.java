package com.coc.user.pay.core.rollback.refund;

import com.coc.user.pay.core.WeiXPay;
import com.coc.user.pay.core.client.WxPayClient;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public abstract class WxRefundRollBack {
    ReentrantLock lock = new ReentrantLock();

    WxPayClient wxPayClient;

    public WxRefundRollBack(WeiXPay weiXPay){
        this.wxPayClient = weiXPay.getWxPayClient();
    }

    public abstract String refundBusiness(Map<String, String> map);

    public void refundNotifyUrl(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = null;
        try {
            params = wxPayClient.getNotifyParameter(request);
            String returnCode = params.get("return_code");
            String resultCode = params.get("result_code");
            if (WXPayConstants.SUCCESS.equals(returnCode) && WXPayConstants.SUCCESS.equals(resultCode)) {
                // 特别提醒：商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致，防止数据泄漏导致出现“假通知”，造成资金损失。
                Map<String, String> map = wxPayClient.decodeRefundNotify(request);
                //避免重复调用数据混乱，需要加锁
                    try{
                        if (lock.tryLock()){
                            //进行业务逻辑处理
                            this.refundBusiness(map);
                        }
                    }finally {
                        lock.unlock();
                    }
                    //以下内容可优化
                    responseWxNotify("SUCCESS","OK",response);

            }else {
                responseWxNotify("FAIL","result_code或return_code不正确",response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void responseWxNotify(String code, String msg, HttpServletResponse response) throws Exception {
        Map<String, String> responseMap = new HashMap<>(2);
        responseMap.put("return_code", "SUCCESS");
        responseMap.put("return_msg", "OK");
        String responseXml = WXPayUtil.mapToXml(responseMap);
        response.setContentType("text/xml");
        response.getWriter().write(responseXml);
        response.flushBuffer();
    }
}
