package com.coc.user.pay.core.mode;


import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.exception.PayException;

import java.util.Map;

/**
 * 公用支付方法
 *
 * @author peace
 */
public abstract class BasePayMode {
    private PayOrderDTO payOrderDTO;

    protected Boolean serviceProvider = false;

    public BasePayMode(PayOrderDTO payOrderDTO) {
        this.payOrderDTO = payOrderDTO;
    }

    /**
     * 支付
     * @return 支付url或html参数
     * @throws PayException 支付异常错误
     */
    public abstract String payment() throws PayException;

    /**
     * 退款
     * @return 退款返回参数
     * @throws PayException 支付异常错误
     * TODO: 2022/11/23 返回值需改为传输对象
     */
    public abstract Map<String, String> refund() throws PayException;

    /**
     * 订单查询
     * @return 订单查询返回参数
     * TODO: 2022/11/23 返回值需改为传输对象
     */
    public abstract Map<String, String> tradeQuery();

    /**
     * 设置订单信息
     */
    void setPayOrder(PayOrderDTO payOrderDTO) {
        this.payOrderDTO = payOrderDTO;
    }

    public PayOrderDTO getPayOrder() {
        return this.payOrderDTO;
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
