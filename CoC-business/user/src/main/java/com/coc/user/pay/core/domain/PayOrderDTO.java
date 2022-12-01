package com.coc.user.pay.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 支付订单信息
 *
 * @author YHF
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderDTO {

    /**
     * 支付信息描述
     */
    private String body;

    /**
     * 标题
     */
    private String subject;

    /**
     * 商户唯一订单号
     */
    private String outTradeNo;

    /**
     * 订单金额
     */
    private String amount;

    /**
     * 用户终端IP
     */
    private String payerClientIp;

    /**
     * 场景类型
     */
    private String type;

    /**
     * 商品信息
     */
    private List<PayGoods> goodsDetail;
}
