package com.halden.TRPG.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.entity.MessageEntity;
import com.halden.TRPG.entity.vo.MessageSendVo;
import com.halden.TRPG.mapper.MessageMapper;
import com.halden.TRPG.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.json.JsonObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageEntity> implements MessageService {
    @Override
    public CodeEnum saveMessage(JSONObject jsonObject) {
        MessageEntity messageEntity = new MessageEntity();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            messageEntity.setCreateTime(dateFormat.parse(jsonObject.getString("createTime")));
            messageEntity.setUid(jsonObject.getString("fromUserId"));
            messageEntity.setUsername(jsonObject.getString("username"));
            messageEntity.setMessage(jsonObject.getString("message"));
            messageEntity.setRid(jsonObject.getLong("rid"));
        } catch (Exception e){
            e.printStackTrace();
        }
        log.info(messageEntity.toString());
        boolean save = save(messageEntity);
        if (save){
            return CodeEnum.SUCCESS;
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }

    @Override
    public CodeEnum getMessage(Long rid) {
        List<MessageEntity> messageEntities = baseMapper.selectList(new QueryWrapper<MessageEntity>()
                .eq("rid", rid).orderByAsc("id"));
        if (messageEntities.size() != 0){
            return CodeEnum.SUCCESS.setData(messageEntities);
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }
//    private List<MessageEntity> messages = new ArrayList<>();
//    private boolean flag = true;


//    @Override
//    public CodeEnum saveMessages(List<MessageSendVo> messageSendVos) {
//        if (flag){
//            save();
//            flag = false;
//        }
//        for (MessageSendVo vo: messageSendVos) {
//            MessageEntity entity = new MessageEntity();
//            BeanUtils.copyProperties(vo,entity);
//            messages.add(entity);
//        }
//        return CodeEnum.SUCCESS;
//    }

//    public void save() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true){
//                    if (messages.size() > 3){
//                        List<MessageEntity> tmp = new ArrayList<>();
//                        synchronized (messages){
//                            for (MessageEntity entity : messages) {
//                                tmp.add(entity);
//                            }
//                            messages.clear();
//                        }
//                        log.info("saving~~~~~~~~~~~~~~");
//                        saveBatch(tmp);
//                        tmp.clear();
//                    }
//                }
//            }
//        }).start();
//    }


}
