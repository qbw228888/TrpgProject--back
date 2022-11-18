package com.halden.TRPG.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
@TableName("room")
public class RoomEntity {
    @TableId(type = IdType.AUTO)
    private Long rid;

    private String roomName;

    private String description;

    private String roomType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT) // 新增的时候填充数据
    private Date createTime;

    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Boolean isDeleted;

    private String ownerUid;

    private Integer size;

    private Integer remain;
}
