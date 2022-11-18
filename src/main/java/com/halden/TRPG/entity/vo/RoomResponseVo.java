package com.halden.TRPG.entity.vo;

import com.halden.TRPG.entity.RoomEntity;
import lombok.Data;

@Data
public class RoomResponseVo extends RoomEntity {
    private String ownerUserName;
    private Boolean ifAdded;
    private Long rid;
}
