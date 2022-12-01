package com.coc.user.pay.core.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户支付后支付宝返回的信息
 *
 * @author yhf
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class AliPayBackDTO extends BasePayBackDTO {

    /**
     * 交易创建时间
     */
    private String gmtCreate;

    /**
     * 字符集
     */
    private String charset;

    /**
     * 卖家邮件
     */
    private String sellerEmail;

    /**
     * 异步响应时间（支付时间）
     */
    private String notifyTime;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 买家支付宝id
     */
    private String buyerId;

    /**
     * 版本
     */
    private String version;

    /**
     * 通知校验 ID
     */
    private String notifyId;

    /**
     * 通知类型
     */
    private String notifyType;

    /**
     * 商家订单号
     */
    private String outTradeNo;

    /**
     * 订单金额
     */
    private String totalAmount;

    /**
     * 交易状态
     * WAIT_BUYER_PAY（交易创建，等待买家付款）
     * TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）
     * TRADE_SUCCESS（交易支付成功）
     * TRADE_FINISHED（交易结束，不可退款）
     */
    private String tradeStatus;

    /**
     * 支付宝交易号
     */
    private String tradeNo;

    /**
     * 授权方的APPID
     */
    private String authAppId;

    /**
     * 买家支付宝账号 id
     */
    private String buyerLogonId;

    /**
     * 支付宝应用的APPID
     */
    private String appId;

    /**
     * 卖家支付宝账号 ID
     */
    private String sellerId;
}
