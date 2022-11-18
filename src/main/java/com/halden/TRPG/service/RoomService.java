package com.halden.TRPG.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.entity.RoomEntity;
import com.halden.TRPG.entity.vo.RoomQueryVo;
import com.halden.TRPG.entity.vo.RoomResponseVo;
import com.halden.TRPG.entity.vo.RoomUpdateVo;
import com.sun.org.apache.bcel.internal.classfile.Code;


public interface RoomService extends IService<RoomEntity> {

    public CodeEnum create(RoomEntity roomEntity, String uid);

    public CodeEnum join(Long rid, String uid);

    public CodeEnum remove(Long rid, String uid);

    public Page<RoomResponseVo> getPage(Integer current, Integer limit, String uid, RoomQueryVo roomQueryVo);
    public Page<RoomResponseVo> getJoinedPage(Integer current, Integer limit, String uid);
    public Page<RoomResponseVo> getCreatedPage(Integer current, Integer limit, String uid);
    public CodeEnum quit(Long rid, String uid);

    public CodeEnum backForJoin(Long rid, String uid, boolean flag);

    public CodeEnum updateRoom(RoomUpdateVo roomUpdateVo, String token);
}
