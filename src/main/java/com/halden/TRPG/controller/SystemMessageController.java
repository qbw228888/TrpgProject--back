package com.halden.TRPG.controller;

import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.common.Result;
import com.halden.TRPG.entity.vo.SystemMessageSendVo;
import com.halden.TRPG.service.SystemMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/message")
@CrossOrigin
@Slf4j
public class SystemMessageController {

    @Autowired
    private SystemMessageService systemMessageService;

    @GetMapping("getList/{uid}")
    public Result getList(@PathVariable("uid") String uid){
        CodeEnum codeEnum = systemMessageService.getList(uid);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200).add("list",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(), codeEnum.getMessage());
        }
    }

    @PostMapping("/sendMessage")
    public Result sendMessage(@RequestBody SystemMessageSendVo vo){
        CodeEnum codeEnum = systemMessageService.sendUserMessage(vo);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200);
        } else {
            return Result.error(codeEnum.getCode(), codeEnum.getMessage());
        }
    }
}
