package com.coc.netty.service;

import com.coc.middleware.pojo.domian.LoginUser;
import com.coc.netty.pojo.dto.MsgDto;
import com.coc.netty.pojo.vo.MsgVo;

import java.util.List;
import java.util.Map;

public interface MsgService {
    MsgVo parseMsg(MsgDto msgDto, String userNickName);
    LoginUser getLoginUser(String token);
    void saveMsg(MsgDto msgDto, LoginUser loginUser);
    Map<Long, List<MsgVo>> findUnreadMsg(LoginUser loginUser);
}
