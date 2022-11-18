package com.halden.TRPG.entity.vo;

import lombok.Data;

@Data
public class RoomUpdateVo {
    private Long rid;
    private String roomName;
    private String description;
    private Integer size;
}
