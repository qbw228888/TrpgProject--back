package com.halden.TRPG.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("room_image")
public class RoomImageEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String img;

    private Long rid;
}
