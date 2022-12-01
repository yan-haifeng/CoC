package com.coc.user.service.lmpl;

import com.coc.user.entity.CocUserEntity;
import com.coc.user.mapper.CocUserMapper;
import com.coc.user.pay.core.domain.WxPayConfig;
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

    public void aa() {
        PayFactory factory = new PayFactory();
        WxPayConfig wxPayConfig = new WxPayConfig();

        factory.getWxPay();
    }

}

