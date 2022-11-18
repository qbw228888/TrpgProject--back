package com.halden.TRPG.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.halden.TRPG.entity.RoomUserEntity;

import java.util.List;

public interface RoomUserService extends IService<RoomUserEntity> {
    public Boolean exists(String uid, Long rid);

    public Boolean add(String uid, Long rid);

    public List<Long> getRidsByUid(String uid);
}
