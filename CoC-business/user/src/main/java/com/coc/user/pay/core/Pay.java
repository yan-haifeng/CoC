package com.coc.user.pay.core;

import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.mode.BasePayMode;

/**
 * 统一支付接口
 * @author yhf
 */
public interface Pay {

    /**
     * 获得H5支付模型
     * @param payOrderDTO 订单传输对象
     * @param returnUrl 支付url
     * @return 支付模型
     */
    BasePayMode getH5Pay(PayOrderDTO payOrderDTO, String returnUrl);

    /**
     * 获得App支付模型
     * @param payOrderDTO 订单传输对象
     * @return 支付模型
     */
    BasePayMode getAppPay(PayOrderDTO payOrderDTO);

    /**
     * 获得PC支付模型
     * @param payOrderDTO 订单传输对象
     * @return 支付模型
     */
    BasePayMode getPcPay(PayOrderDTO payOrderDTO);
}
