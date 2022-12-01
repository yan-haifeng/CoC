package com.coc.user.pay.core.rollback.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.coc.user.pay.core.AliPay;
import com.coc.user.pay.core.constants.PayConstants;
import com.coc.user.pay.core.domain.AliPayBackDTO;
import com.coc.user.pay.core.domain.AliPayConfig;
import com.coc.user.pay.core.utils.HttpUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AliPayRollBack extends PayRollBack{

    ReentrantLock lock = new ReentrantLock();

    AliPayConfig aliPayConfig;

    public AliPayRollBack(AliPay aliPay){
        this.aliPayConfig = aliPay.getAliPayConfigSet();
    }

    public String notifyUrl(HttpServletRequest request, HttpServletResponse response) {
        String result = PayConstants.ALI_RETURN_MESSAGE_FAILURE;
        try {
            //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
            Map<String,String> params = HttpUtils.getParams(request);
            //验签
            boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPayConfig.getAlipayPublicKey(), AlipayConstants.CHARSET_UTF8, AlipayConstants.SIGN_TYPE_RSA2);  //调用SDK验证签名
            if (signVerified){
                // 验签成功
                result =  PayConstants.ALI_RETURN_MESSAGE_SUCCESS;
                AliPayBackDTO aliPayBackDTO = AliPayBackDTO.builder()
                        .gmtCreate(params.get("gmt_create"))
                        .charset(params.get("charset"))
                        .sellerEmail(params.get("seller_email"))
                        .notifyTime(params.get("notify_time"))
                        .subject(params.get("subject"))
                        .buyerId(params.get("buyer_id"))
                        .version(params.get("version"))
                        .notifyId(params.get("notify_id"))
                        .notifyType(params.get("notify_type"))
                        .outTradeNo(params.get("out_trade_no"))
                        .totalAmount(params.get("total_amount"))
                        .tradeStatus(params.get("trade_status"))
                        .tradeNo(params.get("trade_no"))
                        .authAppId(params.get("auth_app_id"))
                        .buyerLogonId(params.get("buyer_logon_id"))
                        .appId(params.get("app_id"))
                        .sellerId(params.get("seller_id"))
                        .build();
                //支付宝可能会调用多次，需要加锁
                if(lock.tryLock()){
                    try {
                        //执行业务逻辑
                        result = this.business(aliPayBackDTO);
                    }finally {
                        lock.unlock();
                    }
                }
            } else {
                // 验签失败则记录异常日志
                result =  PayConstants.ALI_RETURN_MESSAGE_FAILURE;
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setAliPayConfigSet(AliPayConfig aliPayConfig){
        this.aliPayConfig = aliPayConfig;
    }
}
