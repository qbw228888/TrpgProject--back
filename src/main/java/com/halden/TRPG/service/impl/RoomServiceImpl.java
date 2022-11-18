package com.halden.TRPG.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.common.SystemMessageCode;
import com.halden.TRPG.entity.*;
import com.halden.TRPG.entity.vo.RoomQueryVo;
import com.halden.TRPG.entity.vo.RoomResponseVo;
import com.halden.TRPG.entity.vo.RoomUpdateVo;
import com.halden.TRPG.mapper.RoomMapper;
import com.halden.TRPG.service.*;
import com.halden.TRPG.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class RoomServiceImpl extends ServiceImpl<RoomMapper, RoomEntity> implements RoomService {

    @Autowired
    private RoomUserService roomUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SystemMessageService systemMessageService;

    @Autowired
    private ApplyAddRoomService applyAddRoomService;

    @Override
    public CodeEnum create(RoomEntity roomEntity, String uid) {
        UserEntity user = userService.getById(uid);
        if (user == null){
            return CodeEnum.ERROR_PARAMETER_EXCEPTION;
        }
        roomEntity.setOwnerUid(uid);
        roomEntity.setIsDeleted(false);
        roomEntity.setRemain(roomEntity.getSize()-1);
        boolean save1 = save(roomEntity);
        RoomUserEntity roomUserEntity = new RoomUserEntity();
        roomUserEntity.setRid(roomEntity.getRid());
        roomUserEntity.setUid(uid);
        roomUserEntity.setUsername(user.getUsername());
        boolean save2 = roomUserService.save(roomUserEntity);
        if (save1 && save2){
            return CodeEnum.SUCCESS;
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }

    @Override
    public CodeEnum join(Long rid, String uid) {
        RoomEntity room = getById(rid);
        UserEntity applier = userService.getById(uid);
        if (room == null || applier == null){
            return CodeEnum.ERROR_PARAMETER_EXCEPTION;
        }
        if (room.getRemain() <= 0){
            return CodeEnum.ROOM_FULL_EXCEPTION;
        }
        if (roomUserService.exists(uid,rid)){
            return CodeEnum.ALREADY_IN_EXCEPTION;
        }
        if (applyAddRoomService.ifApplied(rid,uid)){
            return CodeEnum.ALREADY_APPLIED_JOIN_ROOM_EXCEPTION;
        }
        String title = applier.getUsername()+"想要加入您的房间";
        String message = applier.getUsername()+"想要加入您创建的房间："+room.getRoomName();
        SystemMessageEntity entity = new SystemMessageEntity();
        entity.setUid(room.getOwnerUid());
        entity.setTitle(title);
        entity.setMessage(message);
        entity.setIsMine(false);
        entity.setCreateTime(new Date());
        entity.setIsViewed(false);
        entity.setMessageType(uid);
        entity.setRid(rid);
        Boolean sendMessage = systemMessageService.sendNormalMessage(entity);
        boolean apply = applyAddRoomService.apply(rid, uid);
        if (sendMessage && apply){
            return CodeEnum.SUCCESS;
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }

    @Override
    public CodeEnum remove(Long rid, String uid) {
        RoomEntity room = getById(rid);
        if (room == null){
            return CodeEnum.ERROR_PARAMETER_EXCEPTION;
        }
        if (!uid.equals(room.getOwnerUid())){
            return CodeEnum.NOT_OWNER_EXCEPTION;
        }
        //发送移出消息
        List<RoomUserEntity> roomUserEntities = roomUserService.list(new QueryWrapper<RoomUserEntity>().eq("rid", rid));
        for (RoomUserEntity item : roomUserEntities){
            if (item.getUid().equals(room.getOwnerUid())){
                continue;
            }
            SystemMessageEntity entity = new SystemMessageEntity();
            entity.setUid(item.getUid());
            entity.setTitle("加入的房间被解散");
            entity.setMessage("您加入的房间" + room.getRoomName() + "已经被解散，您已被自动移出该房间");
            entity.setIsViewed(false);
            entity.setIsMine(false);
            entity.setMessageType("0");
            entity.setCreateTime(new Date());
            Boolean send = systemMessageService.sendNormalMessage(entity);
        }
        //删除三张表的数据
        boolean removeRelation = roomUserService.remove(new QueryWrapper<RoomUserEntity>().eq("rid", rid));
        boolean removeMessage =  messageService.remove(new QueryWrapper<MessageEntity>().eq("rid", rid));
        boolean remove = removeById(rid);
        log.info("remove message: "+removeMessage);
        if (remove && removeRelation){
            return CodeEnum.SUCCESS;
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }

    @Override
    public Page<RoomResponseVo> getPage(Integer current, Integer limit, String uid, RoomQueryVo roomQueryVo) {
        Page<RoomResponseVo> page = new Page<>(current,limit);
        QueryWrapper<RoomEntity> wrapper = new QueryWrapper<>();
        if (roomQueryVo!= null){
            if (StringUtils.hasLength(roomQueryVo.getRoomDetail())){
                wrapper.like("room_name",roomQueryVo.getRoomDetail())
                        .or().like("room_type",roomQueryVo.getRoomDetail())
                        .or().like("description",roomQueryVo.getRoomDetail());
            }
        }
        Page<RoomEntity> tmp = new Page<>(current,limit);
        tmp = page(tmp, wrapper);
        List<RoomEntity> list = tmp.getRecords();
        List<RoomResponseVo> records = list.stream().map(entity -> {
            RoomResponseVo vo = new RoomResponseVo();
            BeanUtils.copyProperties(entity, vo);
            log.info(entity.getOwnerUid());
            vo.setOwnerUserName(userService.getUsername(entity.getOwnerUid()));
            RoomUserEntity relation = roomUserService.getOne(new QueryWrapper<RoomUserEntity>()
                    .eq("rid", vo.getRid())
                    .eq("uid", uid));
            if (relation == null){
                if (applyAddRoomService.ifApplied(vo.getRid(),uid) == false) {
                    vo.setIfAdded(false);
                } else {
                    vo.setIfAdded(true);
                }
            } else {
                vo.setIfAdded(true);
            }
            if (vo.getRemain() <= 0){
                vo.setIfAdded(true);
            }
            return vo;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(tmp,page);
        page.setRecords(records);
        return page;
    }

    @Override
    public Page<RoomResponseVo> getJoinedPage(Integer current, Integer limit, String uid) {
        List<Long> rids = roomUserService.getRidsByUid(uid);
        Page<RoomResponseVo> page = new Page<>(current,limit);
        Page<RoomEntity> tmp = new Page<>(current,limit);
        QueryWrapper<RoomEntity> wrapper = new QueryWrapper<>();
        wrapper.in("rid",rids)
                .ne("owner_uid",uid);
        tmp = page(tmp, wrapper);
        List<RoomEntity> list = tmp.getRecords();
        List<RoomResponseVo> records = list.stream().map(entity -> {
            RoomResponseVo vo = new RoomResponseVo();
            BeanUtils.copyProperties(entity, vo);
            vo.setOwnerUserName(userService.getUsername(entity.getOwnerUid()));
            return vo;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(tmp,page);
        page.setRecords(records);
        return page;
    }

    @Override
    public Page<RoomResponseVo> getCreatedPage(Integer current, Integer limit, String uid) {
        Page<RoomResponseVo> page = new Page<>(current,limit);
        Page<RoomEntity> tmp = new Page<>(current,limit);
        QueryWrapper<RoomEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("owner_uid",uid);
        tmp = page(tmp, wrapper);
        List<RoomEntity> list = tmp.getRecords();
        List<RoomResponseVo> records = list.stream().map(entity -> {
            RoomResponseVo vo = new RoomResponseVo();
            BeanUtils.copyProperties(entity, vo);
            vo.setOwnerUserName(userService.getUsername(entity.getOwnerUid()));
            return vo;
        }).collect(Collectors.toList());
        BeanUtils.copyProperties(tmp,page);
        page.setRecords(records);
        return page;
    }

    @Override
    public CodeEnum quit(Long rid, String uid) {
        UserEntity user = userService.getById(uid);
        boolean remove = roomUserService.remove(new QueryWrapper<RoomUserEntity>()
                .eq("rid", rid)
                .eq("uid", uid));
        RoomEntity roomEntity = getById(rid);
        roomEntity.setRemain(roomEntity.getRemain()+1);
        boolean update = updateById(roomEntity);
        SystemMessageEntity messageEntity = new SystemMessageEntity();
        messageEntity.setMessageType(SystemMessageCode.NORMALMESSAGE.getCode());
        messageEntity.setCreateTime(new Date());
        messageEntity.setUid(roomEntity.getOwnerUid());
        messageEntity.setIsMine(false);
        messageEntity.setIsViewed(false);
        messageEntity.setTitle("退出房间提醒");
        messageEntity.setMessage(user.getUsername()+"退出了您的房间："+roomEntity.getRoomName());
        Boolean sendNormalMessage = systemMessageService.sendNormalMessage(messageEntity);
        if (!remove || !update || !sendNormalMessage){
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
        return CodeEnum.SUCCESS;
    }

    @Override
    public CodeEnum backForJoin(Long rid, String uid, boolean flag) {
        RoomEntity room = getById(rid);
        boolean ifApplied = applyAddRoomService.ifApplied(rid, uid);
        if (ifApplied == false){
            return CodeEnum.NO_APPLY_EXCEPTION;
        }
        boolean remove = applyAddRoomService.remove(new QueryWrapper<ApplyAddRoomEntity>()
                .eq("rid", rid).eq("applier_uid", uid));
        if (!remove){
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
        SystemMessageEntity messageEntity = new SystemMessageEntity();
        messageEntity.setMessageType(SystemMessageCode.NORMALMESSAGE.getCode());
        messageEntity.setCreateTime(new Date());
        messageEntity.setUid(uid);
        messageEntity.setIsMine(false);
        messageEntity.setIsViewed(false);
        if (flag){
            Boolean add = roomUserService.add(uid, rid);
            if (room.getRemain() <= 0){
                return CodeEnum.ROOM_FULL_EXCEPTION;
            }
            room.setRemain(room.getRemain()-1);
            boolean update = updateById(room);
            if (!add || !update){
                return CodeEnum.OPTION_FAIL_EXCEPTION;
            }
            messageEntity.setTitle("加入请求已经通过");
            messageEntity.setMessage("您加入房间: "+room.getRoomName()+"的请求已经通过");
        } else {
            messageEntity.setTitle("加入请求被房主拒绝");
            messageEntity.setMessage("您加入房间: "+room.getRoomName()+"的请求被房主拒绝");
        }
        Boolean sendNormalMessage = systemMessageService.sendNormalMessage(messageEntity);
        if (!sendNormalMessage){
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
        return CodeEnum.SUCCESS;
    }

    @Override
    public CodeEnum updateRoom(RoomUpdateVo roomUpdateVo, String token) {
        RoomEntity entity = getById(roomUpdateVo.getRid());
        String uid = JwtUtil.getUid(token);
        log.info(uid);
        if (!uid.equals(entity.getOwnerUid())){
            return CodeEnum.NOT_OWNER_EXCEPTION;
        }
        if (entity == null){
            return CodeEnum.VAILD_EXCEPTION;
        }
        if (StringUtils.hasLength(roomUpdateVo.getRoomName())){
            entity.setRoomName(roomUpdateVo.getRoomName());
        }
        if (StringUtils.hasLength(roomUpdateVo.getDescription())){
            entity.setDescription(roomUpdateVo.getDescription());
        }
        if (roomUpdateVo.getSize() > entity.getSize()){
            int t = roomUpdateVo.getSize() - entity.getSize();
            entity.setSize(roomUpdateVo.getSize());
            entity.setRemain(entity.getRemain()+t);
        }
        boolean flag = updateById(entity);
        if (flag){
            return CodeEnum.SUCCESS;
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }
}
