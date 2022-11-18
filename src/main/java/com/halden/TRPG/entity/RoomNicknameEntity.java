package com.halden.TRPG.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("room_nickname")
public class RoomNicknameEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long rid;

    private String uid;

    private String nickname;
}
