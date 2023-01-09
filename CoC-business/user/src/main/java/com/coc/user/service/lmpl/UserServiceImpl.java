package com.coc.user.service.lmpl;

import com.coc.user.entity.CocUserEntity;
import com.coc.user.mapper.CocUserMapper;
import com.coc.user.pay.core.WeiXPay;
import com.coc.user.pay.core.domain.PayGoods;
import com.coc.user.pay.core.domain.PayOrderDTO;
import com.coc.user.pay.core.domain.WxPayConfig;
import com.coc.user.pay.core.exception.PayException;
import com.coc.user.pay.core.factory.PayFactory;
import com.coc.user.pojo.dto.UserDto;
import com.coc.user.service.UserService;
import com.coc.user.wrapper.CocUserQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private CocUserMapper cocUserMapper;

    /**
     * 根据用户名查询用户信息
     */
    @Override
    public UserDto findUserByUserName(String userName){
        CocUserQuery query = new CocUserQuery().where.userName().eq(userName).end();
        CocUserEntity user = cocUserMapper.findOne(query);
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    /**
     * 根据用户id查询用户信息
     */
    @Override
    public UserDto findUserByUserId(Long userId) {
        CocUserQuery query = new CocUserQuery().where.userId().eq(userId).end();
        CocUserEntity user = cocUserMapper.findOne(query);
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    /**
     * 查询所有用户
     */
    @Override
    public List<UserDto> findUser() {
        CocUserQuery query = new CocUserQuery().where.isDelete().eq(0).status().eq(0).end();
        List<CocUserEntity> list = cocUserMapper.listEntity(query);
        List<UserDto> dtoList = new ArrayList<>();
        list.forEach(cocUserEntity ->{
            UserDto dto = new UserDto();
            BeanUtils.copyProperties(cocUserEntity, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    @Override
    public String payTest() throws PayException {
        PayFactory factory = new PayFactory();
        WxPayConfig config = new WxPayConfig();
        config.setMchID("1623380336");
        config.setAppID("wxd8179fb45fa501b5");
        config.setKey("Year95622huangshihan941bdqgtsydh");
        config.setCertPath("cert/apiclient_cert.p12");
        config.setPayNotifyUrl("/user/pay/back");
        WeiXPay wxPay = (WeiXPay) factory.getWxPay(config);
        PayOrderDTO order = new PayOrderDTO();
        order.setBody("测试订单1");
        order.setAmount("1");
        order.setSubject("测试订单1");
        order.setOutTradeNo("65483221544886654");
        order.setPayerClientIp("127.0.0.1");
        List<PayGoods> goodsList = new ArrayList<>();
        PayGoods goods = PayGoods.builder()
                .goodsId("001")
                .wxPayGoodsId("001")
                .goodsCategory("box")
                .goodsName("纸箱")
                .quantity("1")
                .price("1")
                .build();
        goodsList.add(goods);
        order.setGoodsDetail(goodsList);
        return wxPay.getPcPay(order).payment();
    }

}

