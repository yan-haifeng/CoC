package com.coc.netty.service.serviceImpl;

import com.coc.middleware.pojo.domian.LoginUser;
import com.coc.middleware.utils.AuthUtils;
import com.coc.netty.entity.CocChartMsgEntity;
import com.coc.netty.mapper.CocChartMsgMapper;
import com.coc.netty.pojo.dto.MsgDto;
import com.coc.netty.pojo.vo.MsgVo;
import com.coc.netty.service.MsgService;
import com.coc.netty.wrapper.CocChartMsgQuery;
import com.coc.remote.client.UserClient;
import com.coc.remote.dto.UserDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class MsgServiceImpl implements MsgService {
    @Resource
    private UserClient userClient;

    @Resource
    private AuthUtils authUtils;

    @Resource
    private CocChartMsgMapper cocChartMsgMapper;

    /**
     * 包装返回消息
     */
    @Override
    public MsgVo parseMsg(MsgDto msgDto, String userNickName){
        MsgVo msgVo = new MsgVo();
        msgVo.setMsg(msgDto.getMsg());
        msgVo.setSendUserTaskName(userNickName);
        msgVo.setSendTime(new Date());
        return msgVo;
    }

    public MsgVo parseMsg(CocChartMsgEntity msgEntity) {
        MsgVo msgVo = new MsgVo();
        msgVo.setMsg(msgEntity.getMsg());
        UserDto user = userClient.getUserByUserId(msgEntity.getSendUserId());
        msgVo.setSendUserTaskName(user.getNickName());
        msgVo.setSendTime(msgEntity.getSendTime());
        return msgVo;
    }

    /**
     * 获得登录用户
     */
    @Override
    public LoginUser getLoginUser(String token) throws RuntimeException{
        //获取用户id
        return authUtils.getLoginUser(token);
    }

    /**
     * 保存消息
     */
    public void saveMsg(MsgDto msgDto, LoginUser loginUser){
        CocChartMsgEntity cocChartMsg = new CocChartMsgEntity();
        cocChartMsg.setReceiveUserId(Long.parseLong(msgDto.getReceiveUserId()));
        cocChartMsg.setSendUserId(loginUser.getUser().getUserId());
        cocChartMsg.setMsg(msgDto.getMsg());
        cocChartMsg.setSendTime(new Date());

        cocChartMsgMapper.save(cocChartMsg);
    }

    /**
     * 查找未读消息
     */
    public Map<Long, List<MsgVo>> findUnreadMsg(LoginUser loginUser){
        CocChartMsgQuery query = new CocChartMsgQuery();
        query.where.receiveUserId().eq(loginUser.getUser().getUserId()).status().eq(0).isDelete().eq(0).end();
        List<CocChartMsgEntity> list = cocChartMsgMapper.listEntity(query);
        Map<Long, List<MsgVo>> map = new HashMap<>();
        list.forEach(CocChartMsgEntity -> {
            if(!map.containsKey(CocChartMsgEntity.getSendUserId())){
                List<MsgVo> entityList = new ArrayList<>();
                entityList.add(parseMsg(CocChartMsgEntity));
                map.put(CocChartMsgEntity.getSendUserId(), entityList);
            }else{
                map.get(CocChartMsgEntity.getSendUserId()).add(parseMsg(CocChartMsgEntity));
            }
            this.setStatus(1, CocChartMsgEntity.getId());
        });
        return map;
    }

    public void setStatus(int val, int id){
        CocChartMsgEntity cocChartMsg = new CocChartMsgEntity();
        cocChartMsg.setId(id);
        cocChartMsg.setStatus(val);
        cocChartMsgMapper.updateById(cocChartMsg);
    }

}
