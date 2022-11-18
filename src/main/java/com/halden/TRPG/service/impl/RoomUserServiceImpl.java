package com.halden.TRPG.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halden.TRPG.entity.RoomUserEntity;
import com.halden.TRPG.mapper.RoomUserMapper;
import com.halden.TRPG.service.RoomUserService;
import com.halden.TRPG.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomUserServiceImpl extends ServiceImpl<RoomUserMapper, RoomUserEntity>
        implements RoomUserService {
    @Autowired
    private UserService userService;

    @Override
    public Boolean exists(String uid, Long rid) {
        QueryWrapper<RoomUserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("uid",uid);
        wrapper.eq("rid",rid);
        RoomUserEntity one = getOne(wrapper);
        if (one == null){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Boolean add(String uid, Long rid) {
        RoomUserEntity entity = new RoomUserEntity();
        entity.setUid(uid);
        entity.setRid(rid);
        entity.setUsername(userService.getUsername(uid));
        return save(entity);
    }

    @Override
    public List<Long> getRidsByUid(String uid) {
        List<RoomUserEntity> roomUserEntities = baseMapper.selectList(new QueryWrapper<RoomUserEntity>().eq("uid", uid));
        List<Long> rids = roomUserEntities.stream().map(entity -> {
            return entity.getRid();
        }).collect(Collectors.toList());
        return rids;
    }
}
