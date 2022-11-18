package com.halden.TRPG.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.halden.TRPG.entity.ApplyAddRoomEntity;

public interface ApplyAddRoomService extends IService<ApplyAddRoomEntity> {

    public boolean apply(Long rid, String uid);

    public boolean ifApplied(Long rid, String uid);
}
