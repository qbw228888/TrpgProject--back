package com.halden.TRPG.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.entity.UserEntity;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface UserService extends IService<UserEntity> {

    public List<String> getUid(String username);

    public String getUsername(String uid);

    CodeEnum saveUser(UserEntity entity);

    UserEntity getByPhone(String phone);

    CodeEnum logIn(UserEntity entity);

    CodeEnum uploadAvatar(MultipartFile avatar, String token);

    boolean setAvatar(String uid, String avatarPath);

    CodeEnum getOne(String uid);

    CodeEnum updateUser(UserEntity entity);

    CodeEnum getAvatar(String uid);
}
