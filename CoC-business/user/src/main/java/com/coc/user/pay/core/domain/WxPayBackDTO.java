package com.coc.user.pay.core.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微信支付回调传输对象
 *
 * @author yhf
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class WxPayBackDTO extends BasePayBackDTO {

    /**
     * 微信支付订单号
     */
    private String transactionId;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 用户标识
     */
    private String openId;

    /**
     * 货币种类
     */
    private String feeType;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 子商户号
     */
    private String subMchId;

    /**
     * 设备号 微信支付分配的终端设备号
     */
    private String deviceInfo;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 订单金额
     */
    private String totalFee;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 业务结果
     */
    private String resultCode;

    /**
     * 支付完成时间
     */
    private String timeEnd;

    /**
     * 返回状态码
     */
    private String returnCode;
}
