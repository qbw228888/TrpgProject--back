package com.halden.TRPG.controller;

import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.common.Result;
import com.halden.TRPG.entity.vo.MessageSendVo;
import com.halden.TRPG.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
@CrossOrigin
public class MessageController {

    @Autowired
    MessageService messageService;
//
//    @PostMapping("/send")
//    public Result send(@RequestBody(required = true) List<MessageSendVo> messageSendVoList){
//        CodeEnum send = messageService.saveMessages(messageSendVoList);
//        return Result.ok(200,"发送成功");
//    }

    @GetMapping("/get/{rid}")
    public Result get(@PathVariable("rid") Long rid){
        CodeEnum codeEnum = messageService.getMessage(rid);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200).add("data",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(),codeEnum.getMessage());
        }
    }

}
