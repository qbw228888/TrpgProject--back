package com.halden.TRPG.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.entity.MessageEntity;
import com.halden.TRPG.entity.RoomNicknameEntity;
import com.halden.TRPG.mapper.RoomNicknameMapper;
import com.halden.TRPG.service.MessageService;
import com.halden.TRPG.service.RoomNicknameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomNicknameServiceImpl extends ServiceImpl<RoomNicknameMapper, RoomNicknameEntity>
        implements RoomNicknameService {

    @Autowired
    private MessageService messageService;

    @Override
    public CodeEnum getNickname(Long rid, String uid) {
        RoomNicknameEntity entity = baseMapper.selectOne(new QueryWrapper<RoomNicknameEntity>()
                .eq("rid", rid).eq("uid", uid));
        return CodeEnum.SUCCESS.setData(entity);
    }

    @Override
    public CodeEnum setNickname(Long rid, String uid, String nickname) {
        RoomNicknameEntity entity = baseMapper.selectOne(new QueryWrapper<RoomNicknameEntity>()
                .eq("rid", rid).eq("uid", uid));
        boolean flag = false;
        if (entity == null){
            entity = new RoomNicknameEntity();
            entity.setRid(rid);
            entity.setUid(uid);
            entity.setNickname(nickname);
            flag = save(entity);
        } else {
            entity.setNickname(nickname);
            flag = updateById(entity);
        }
        List<MessageEntity> messageEntityList = messageService.list(new QueryWrapper<MessageEntity>().eq("rid", rid).eq("uid", uid));
        for (MessageEntity messageEntity: messageEntityList){
            messageEntity.setUsername(nickname);
        }
        boolean updateBatch = messageService.updateBatchById(messageEntityList);
        flag = flag && updateBatch;
        if (flag){
            return CodeEnum.SUCCESS;
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }
}
