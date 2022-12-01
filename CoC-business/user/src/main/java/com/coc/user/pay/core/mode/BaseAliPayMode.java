package com.coc.user.pay.core.mode;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeFastpayRefundQueryModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.coc.user.pay.core.domain.AliPayConfig;
import com.coc.user.pay.core.domain.PayGoods;
import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.exception.PayException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 支付宝支付公共方法
 *
 * @author yhf
 */
public abstract class BaseAliPayMode extends BasePayMode {

    /**
     * 设置支付宝配置参数
     */
    private final AliPayConfig aliPayConfig;

    private final AlipayClient alipayClient;

    protected Boolean useAuthToken = false;

    public BaseAliPayMode(PayOrderDTO payOrderDTO, AliPayConfig aliPayConfig, AlipayClient alipayClient) {
        super(payOrderDTO);
        this.aliPayConfig = aliPayConfig;
        this.alipayClient = alipayClient;
    }

    public AliPayConfig getAliPayConfigSet() {
        return this.aliPayConfig;
    }

    public AlipayClient getAlipayClient() {
        return this.alipayClient;
    }

    @Override
    public Map<String, String> tradeQuery() {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(getPayOrder().getOutTradeNo());
        request.setBizModel(model);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(alipayClient.execute(request).getBody());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        // TODO: 2022/11/23 tradeQuery 得把返回map改为传输对象 yhf
        return (Map<String, String>) jsonObject.getInnerMap().get("alipay_trade_query_response");
    }

    @Override
    public Map<String, String> refund() {
        Map<String, String> result = new HashMap<>();
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(getPayOrder().getOutTradeNo());
        model.setRefundAmount(new BigDecimal(getPayOrder().getAmount()).divide(new BigDecimal(100), RoundingMode.UNNECESSARY).doubleValue() + "");
        request.setBizModel(model);
        try {
            alipayClient.execute(request);
            // 信任查询退款的信息
            result = refundQuery();
        } catch (AlipayApiException | PayException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Map<String, String> refundQuery() throws PayException {
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setOutTradeNo(getPayOrder().getOutTradeNo());
        model.setOutRequestNo(getPayOrder().getOutTradeNo());
        request.setBizModel(model);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(alipayClient.execute(request).getBody());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        // TODO: 2022/11/23 refundQuery 得把返回map改为传输对象 yhf
        return (Map<String, String>) jsonObject.getInnerMap().get("alipay_trade_fastpay_refund_query_response");
    }

    public List<GoodsDetail> getGoodsDetail() {
        List<PayGoods> goodsDetail = getPayOrder().getGoodsDetail();

        return goodsDetail.stream().map(p -> {
            GoodsDetail detail = new GoodsDetail();
            detail.setGoodsId(p.getGoodsId());
            detail.setGoodsName(p.getGoodsName());
            detail.setQuantity(Long.parseLong(p.getQuantity()));
            detail.setPrice(p.getPrice());
            return detail;
        }).collect(Collectors.toList());
    }
}
