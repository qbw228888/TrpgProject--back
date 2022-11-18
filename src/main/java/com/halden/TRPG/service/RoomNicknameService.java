package com.halden.TRPG.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.entity.RoomNicknameEntity;

public interface RoomNicknameService extends IService<RoomNicknameEntity> {

    public CodeEnum getNickname(Long rid, String uid);

    public CodeEnum setNickname(Long rid, String uid, String nickname);
}
