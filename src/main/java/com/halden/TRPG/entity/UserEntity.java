package com.halden.TRPG.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class UserEntity {
    @TableId(type = IdType.ASSIGN_UUID)
    private String uid;

    private String username;

    private String password;

    private String phone;

    private String avatarUrl;

    @TableLogic
    private Boolean isDeleted;
}
