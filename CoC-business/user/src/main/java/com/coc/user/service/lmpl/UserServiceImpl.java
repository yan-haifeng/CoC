package com.coc.user.service.lmpl;

import com.coc.user.entity.CocUserEntity;
import com.coc.user.mapper.CocUserMapper;
import com.coc.user.pojo.dto.UserDto;
import com.coc.user.service.UserService;
import com.coc.user.wrapper.CocUserQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
