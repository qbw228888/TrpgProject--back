package com.halden.TRPG.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.common.Result;
import com.halden.TRPG.entity.RoomEntity;
import com.halden.TRPG.entity.RoomMusicEntity;
import com.halden.TRPG.entity.vo.RoomMusicUploadVo;
import com.halden.TRPG.entity.vo.RoomQueryVo;
import com.halden.TRPG.entity.vo.RoomResponseVo;
import com.halden.TRPG.entity.vo.RoomUpdateVo;
import com.halden.TRPG.service.RoomImageService;
import com.halden.TRPG.service.RoomMusicService;
import com.halden.TRPG.service.RoomNicknameService;
import com.halden.TRPG.service.RoomService;
import com.halden.TRPG.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.Path;
import java.sql.Blob;
import java.util.HashMap;

@RestController
@RequestMapping("/room")
@CrossOrigin
@Slf4j
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomImageService roomImageService;

    @Autowired
    private RoomMusicService roomMusicService;

    @Autowired
    private RoomNicknameService roomNicknameService;

    @PostMapping("/create/{uid}")
    public Result createRoom(@RequestBody RoomEntity roomEntity,
                             @PathVariable("uid") String uid) {
        CodeEnum code = roomService.create(roomEntity, uid);
        if (code == CodeEnum.SUCCESS) {
            return Result.ok(200, "创建房间成功");
        }
        return Result.error(code.getCode(),code.getMessage());
    }

    @PostMapping("/join/{rid}/{uid}")
    public Result joinRoom(@PathVariable("rid") Long rid,
                           @PathVariable("uid") String uid){
        CodeEnum join = roomService.join(rid, uid);
        if (join == CodeEnum.SUCCESS){
            return Result.ok(200,"已向房主发送加入房间申请");
        }
        return Result.error(join.getCode(),join.getMessage());
    }

    @PostMapping("/remove/{rid}/{uid}")
    public Result removeRoom(@PathVariable("rid") Long rid,
                             @PathVariable("uid") String uid,
                             @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        CodeEnum remove = roomService.remove(rid, uid);
        if (remove == CodeEnum.SUCCESS){
            return Result.ok(200,"删除房间成功");
        }
        return Result.error(remove.getCode(),remove.getMessage());
    }

    @PostMapping("/page/{current}/{limit}/{uid}")
    public Result getPage(@PathVariable("current") Integer current,
                          @PathVariable("limit") Integer limit,
                          @PathVariable("uid") String uid,
                          @RequestBody(required = false) RoomQueryVo roomQueryVo,
                          @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        Page<RoomResponseVo> page = roomService.getPage(current, limit, uid, roomQueryVo);
        return Result.ok(200).add("page", page);
    }

    @PostMapping("/back/join/{rid}/{uid}/{back}")
    public Result backForApplyJoinRoom(@PathVariable("rid") Long rid,
                                       @PathVariable("uid") String uid,
                                       @PathVariable("back") Boolean back){
        CodeEnum codeEnum = roomService.backForJoin(rid, uid, back);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200);
        } else {
            return Result.error(codeEnum.getCode(),codeEnum.getMessage());
        }
    }

    @GetMapping("/joined/{current}/{limit}/{uid}")
    public Result getJoinedPage(@PathVariable("current") Integer current,
                                @PathVariable("limit") Integer limit,
                                @PathVariable("uid") String uid,
                                @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        Page<RoomResponseVo> joinedPage = roomService.getJoinedPage(current, limit, uid);
        return Result.ok(200).add("page",joinedPage);
    }

    @GetMapping("/created/{current}/{limit}/{uid}")
    public Result getCreatedPage(@PathVariable("current") Integer current,
                                @PathVariable("limit") Integer limit,
                                @PathVariable("uid") String uid,
                                @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        Page<RoomResponseVo> page = roomService.getCreatedPage(current, limit, uid);
        return Result.ok(200).add("page",page);
    }

    @PostMapping("/quit/{rid}/{uid}")
    public Result quit(@PathVariable("rid") Long rid,
                       @PathVariable("uid") String uid,
                       @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        if (!JwtUtil.getUid(token).equals(uid)){
            return Result.error(401,"非本人操作");
        }
        CodeEnum codeEnum = roomService.quit(rid, uid);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200);
        } else {
            return Result.error(codeEnum.getCode(), codeEnum.getMessage());
        }
    }

    @PostMapping("/image/upload/{rid}")
    public Result uploadImage(@RequestPart("file") MultipartFile image,
                               @PathVariable("rid") Long rid,
                               @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        CodeEnum codeEnum = roomImageService.uploadImage(image, rid);
        if (codeEnum == CodeEnum.SUCCESS) {
            return Result.ok(200).add("data",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(),codeEnum.getMessage());
        }
    }

    @GetMapping("/image/get/{rid}")
    public Result getImages(@PathVariable("rid") Long rid){
        CodeEnum codeEnum = roomImageService.getImages(rid);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200).add("data",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(), codeEnum.getMessage());
        }
    }

    @PostMapping("/image/delete/{rid}")
    public Result deleteImage(@PathVariable("rid") Long rid,
                              @RequestBody String url){
        CodeEnum codeEnum = roomImageService.deleteImage(rid, url);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200);
        } else {
            return Result.error(codeEnum.getCode(), codeEnum.getMessage());
        }
    }

    @PostMapping("/music/create")
    public Result createMusic(@RequestBody RoomMusicUploadVo vo,
                              @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        CodeEnum codeEnum = roomMusicService.createMusic(vo);
        if (codeEnum == CodeEnum.SUCCESS) {
            return Result.ok(200).add("data",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(),codeEnum.getMessage());
        }
    }

    @PostMapping("/music/upload/{rid}/{id}")
    public Result uploadMusic(@RequestPart("file") MultipartFile music,
                              @PathVariable("rid") Long rid,
                              @PathVariable("id") Long id,
                              @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        CodeEnum codeEnum = roomMusicService.uploadMusic(music,rid,id);
        if (codeEnum == CodeEnum.SUCCESS) {
            return Result.ok(200).add("data",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(),codeEnum.getMessage());
        }
    }

    @PostMapping("/music/upload/cover/{id}")
    public Result uploadCover(@RequestPart("file") MultipartFile cover,
                              @PathVariable("id") Long id,
                              @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        CodeEnum codeEnum = roomMusicService.uploadCover(cover, id);
        if (codeEnum == CodeEnum.SUCCESS) {
            return Result.ok(200).add("data",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(),codeEnum.getMessage());
        }
    }


    @GetMapping("/music/get/{rid}")
    public Result getMusics(@PathVariable("rid") Long rid){
        CodeEnum codeEnum = roomMusicService.getMusics(rid);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200).add("data",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(), codeEnum.getMessage());
        }
    }

    @PostMapping("/music/delete/{rid}/{name}")
    public Result deleteMusic(@PathVariable("rid") Long rid,
                              @PathVariable("name") String name,
                              @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        CodeEnum codeEnum = roomMusicService.deleteMusic(rid, name);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200);
        } else {
            return Result.error(codeEnum.getCode(), codeEnum.getMessage());
        }
    }


    @GetMapping("/nickname/get/{rid}/{uid}")
    public Result getNickname(@PathVariable("rid") Long rid,
                              @PathVariable("uid") String uid){
        CodeEnum codeEnum = roomNicknameService.getNickname(rid, uid);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200).add("data",codeEnum.getData());
        } else {
            return Result.error(codeEnum.getCode(), codeEnum.getMessage());
        }
    }

    @PostMapping("/nickname/set/{rid}/{uid}/{nickname}")
    public Result setNickname(@PathVariable("rid") Long rid,
                              @PathVariable("uid") String uid,
                              @PathVariable("nickname") String nickname){
        CodeEnum codeEnum = roomNicknameService.setNickname(rid, uid, nickname);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200);
        } else {
            return Result.error(codeEnum.getCode(), codeEnum.getMessage());
        }
    }

    @PostMapping("/update")
    public Result update(@RequestBody RoomUpdateVo roomUpdateVo,
                         @RequestHeader(value = "Authorization") String token){
        if (!JwtUtil.check(token)){
            return Result.error(401,"token过期，请重新登录");
        }
        CodeEnum codeEnum = roomService.updateRoom(roomUpdateVo, token);
        if (codeEnum == CodeEnum.SUCCESS){
            return Result.ok(200);
        } else {
            return Result.error(codeEnum.getCode(), codeEnum.getMessage());
        }
    }
}
