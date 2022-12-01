package com.coc.user.pay.core.factory;

import com.coc.user.pay.core.AliPay;
import com.coc.user.pay.core.WeiXPay;
import com.coc.user.pay.core.Pay;
import com.coc.user.pay.core.domain.AliPayConfig;
import com.coc.user.pay.core.domain.WxPayConfig;

/**
 * 支付工厂
 * @author peace
 */
public class PayFactory{
    private Pay pay;

    public Pay getAliPay(AliPayConfig aliPayConfig){
        return new AliPay(aliPayConfig);
    }

    public Pay getWxPay(WxPayConfig wxPayConfig){
        return new WeiXPay(wxPayConfig);
    }
}
