package com.halden.TRPG.controller;

import com.halden.TRPG.service.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/websocket")
public class WebSocketController {
    @GetMapping("test")
    public void test() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String date = dateFormat.format(new Date());
            WebSocketServer.sendInfo("后端服务推送信息:"+date,"user1234");
        } catch (Exception e) {
            log.error("分页查询数据接口列表失败", e);
        }
    }

}
