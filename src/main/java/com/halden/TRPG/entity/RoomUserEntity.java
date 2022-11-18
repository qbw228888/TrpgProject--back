package com.halden.TRPG.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("room_user")
public class RoomUserEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String uid;

    private Long rid;

    private String username;
}
