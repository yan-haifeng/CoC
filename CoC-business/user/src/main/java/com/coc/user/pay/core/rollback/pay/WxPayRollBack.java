package com.coc.user.pay.core.rollback.pay;

import com.coc.user.pay.core.WeiXPay;
import com.coc.user.pay.core.client.WxPayClient;
import com.coc.user.pay.core.domain.WxPayBackDTO;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public abstract class WxPayRollBack extends PayRollBack{

    ReentrantLock lock = new ReentrantLock();

    WxPayClient wxPayClient;

    public WxPayRollBack(WeiXPay weiXPay){
        this.wxPayClient = weiXPay.getWxPayClient();
    }

    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = null;
        try {
            params = wxPayClient.getNotifyParameter(request);
            String returnCode = params.get("return_code");
            String resultCode = params.get("result_code");
            if (WXPayConstants.SUCCESS.equals(returnCode) && WXPayConstants.SUCCESS.equals(resultCode)) {
                // 特别提醒：商户系统对于支付结果通知的内容一定要做签名验证,并校验返回的订单金额是否与商户侧的订单金额一致，防止数据泄漏导致出现“假通知”，造成资金损失。
                boolean signatureValid = wxPayClient.isPayResultNotifySignatureValid(params);
                if (signatureValid) {
                    WxPayBackDTO wxPayBackDTO = WxPayBackDTO.builder()
                            .transactionId(params.get("transaction_id"))
                            .nonceStr(params.get("nonce_str"))
                            .openId(params.get("openid"))
                            .feeType(params.get("fee_type"))
                            .mchId(params.get("mch_id"))
                            .subMchId(params.get("sub_mch_id"))
                            .deviceInfo(params.get("device_info"))
                            .outTradeNo(params.get("out_trade_no"))
                            .totalFee(params.get("total_fee"))
                            .tradeType(params.get("trade_type"))
                            .resultCode(params.get("result_code"))
                            .timeEnd(params.get("time_end"))
                            .returnCode(params.get("return_code")).build();
                    //避免重复调用数据混乱，需要加锁
                    try{
                        if (lock.tryLock()){
                            //保证幂等性，需要查询支付状态
                            //进行业务逻辑处理
                            this.business(wxPayBackDTO);
                        }
                    }finally {
                        lock.unlock();
                    }
                    //以下内容可优化
                    responseWxNotify("SUCCESS","OK",response);
                }else {
                    responseWxNotify("FAIL","签名验证错误",response);
                }
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
