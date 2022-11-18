package com.halden.TRPG.controller;

import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.common.Result;
import com.halden.TRPG.entity.UserEntity;
import com.halden.TRPG.service.UserService;
import com.halden.TRPG.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@CrossOrigin
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody UserEntity userEntity){
        CodeEnum codeEnum = userService.saveUser(userEntity);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200,"register success").add("data",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(),codeEnum.getMessage());
        }
    }

    @PostMapping("/logIn")
    public Result logIn(@RequestBody UserEntity userEntity){
        CodeEnum codeEnum = userService.logIn(userEntity);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200,"log in success").add("data",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(),codeEnum.getMessage());
        }
    }

    @PostMapping("/avatar/upload")
    public Result uploadAvatar(@RequestPart("file") MultipartFile avatar,
                               @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        CodeEnum codeEnum = userService.uploadAvatar(avatar, token);
        if (codeEnum == CodeEnum.SUCCESS) {
            return Result.ok(200);
        } else {
            return Result.error(codeEnum.getCode(),codeEnum.getMessage());
        }
    }

    @GetMapping("/getOne/{uid}")
    public Result getOne(@PathVariable("uid") String uid,
                         @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        CodeEnum codeEnum = userService.getOne(uid);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200).add("data",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(),codeEnum.getMessage());
        }
    }

    @PostMapping("/save")
    public Result saveUser(@RequestBody UserEntity entity){
        CodeEnum codeEnum = userService.updateUser(entity);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200);
        } else {
            return Result.error(codeEnum.getCode(), codeEnum.getMessage());
        }
    }

    @GetMapping("/avatar/get/{uid}")
    public Result getAvatar(@PathVariable("uid") String uid){
        CodeEnum codeEnum = userService.getAvatar(uid);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200).add("data",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(), codeEnum.getMessage());
        }
    }
}
