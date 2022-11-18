package com.halden.TRPG.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.entity.MessageEntity;
import com.halden.TRPG.entity.vo.MessageSendVo;

import javax.json.JsonObject;
import java.util.List;

public interface MessageService extends IService<MessageEntity> {
//    public CodeEnum saveMessages(List<MessageSendVo> messageSendVos);

    public CodeEnum saveMessage(JSONObject jsonObject);

    public CodeEnum getMessage(Long rid);
}
