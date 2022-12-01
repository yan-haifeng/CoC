package com.coc.user.pay.core.domain;

import lombok.Builder;
import lombok.Data;

/**
 * 支付商品信息
 *
 * @author yhf
 */
@Data
@Builder
public class PayGoods {

    /**
     * id
     */
    private String goodsId;

    /**
     * 微信商品id
     */
    private String wxPayGoodsId;

    /**
     * 商品名
     */
    private String goodsName;

    /**
     * 数量
     */
    private String quantity;

    /**
     * 价格
     */
    private String price;

    /**
     * 类别
     */
    private String goodsCategory;

    /**
     * 备注
     */
    private String body;
}
