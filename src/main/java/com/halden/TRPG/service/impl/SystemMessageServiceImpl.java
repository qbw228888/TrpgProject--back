package com.halden.TRPG.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.common.SystemMessageCode;
import com.halden.TRPG.entity.MessageEntity;
import com.halden.TRPG.entity.RoomEntity;
import com.halden.TRPG.entity.SystemMessageEntity;
import com.halden.TRPG.entity.UserEntity;
import com.halden.TRPG.entity.vo.SystemMessageResponseVo;
import com.halden.TRPG.entity.vo.SystemMessageSendVo;
import com.halden.TRPG.mapper.SystemMessageMapper;
import com.halden.TRPG.service.RoomService;
import com.halden.TRPG.service.SystemMessageService;
import com.halden.TRPG.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class SystemMessageServiceImpl extends ServiceImpl<SystemMessageMapper, SystemMessageEntity>
        implements SystemMessageService {

    @Override
    public Boolean sendNormalMessage(SystemMessageEntity entity) {
        if (entity.getRid() == null){
            entity.setRid(new Long(0));
        }
        boolean save = save(entity);
        return save;
    }

    @Override
    public CodeEnum sendUserMessage(SystemMessageSendVo vo) {
        SystemMessageEntity entity = new SystemMessageEntity();
        entity.setUid(vo.getUid());
        entity.setTitle(vo.getTitle());
        entity.setMessage(vo.getMessage());
        entity.setCreateTime(vo.getCreateTime());
        entity.setIsViewed(true);
        entity.setIsMine(true);
        entity.setMessageType(SystemMessageCode.NORMALMESSAGE.getCode());
        Boolean send = sendNormalMessage(entity);
        if (send == true){
            return CodeEnum.SUCCESS;
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }

    @Override
    public CodeEnum getList(String uid) {
        List<SystemMessageEntity> entities = baseMapper.selectList(new QueryWrapper<SystemMessageEntity>().eq("uid", uid).last("limit 10"));
        if (entities.size() == 0){
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
        List<SystemMessageResponseVo> vos = new ArrayList<>();
        for (SystemMessageEntity entity : entities){
            entity.setIsViewed(true);
            SystemMessageResponseVo vo = new SystemMessageResponseVo();
            vo.setDate(entity.getCreateTime());
            vo.setTitle(entity.getTitle());
            vo.setMessage(entity.getMessage());
            vo.setMine(entity.getIsMine());
            vo.setMessageType(entity.getMessageType());
            vo.setRid(entity.getRid());
            vos.add(vo);
        }
        boolean b = updateBatchById(entities);
        if (b) {
            return CodeEnum.SUCCESS.setData(vos);
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }

}
