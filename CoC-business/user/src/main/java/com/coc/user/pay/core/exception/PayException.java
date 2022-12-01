package com.coc.user.pay.core.exception;

/**
 * 支付异常类
 *
 * @author peace
 */
public class PayException extends Exception {
    public PayException(String s) {
        super("支付时出现异常：" + s);
    }
}
