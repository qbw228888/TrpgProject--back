package com.halden.TRPG.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.entity.SystemMessageEntity;
import com.halden.TRPG.entity.vo.SystemMessageSendVo;

import java.util.Date;

public interface SystemMessageService extends IService<SystemMessageEntity> {

    public Boolean sendNormalMessage(SystemMessageEntity entity);

    public CodeEnum sendUserMessage(SystemMessageSendVo vo);

    public CodeEnum getList(String uid);
}
