package com.halden.TRPG.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.entity.UserEntity;
import com.halden.TRPG.mapper.UserMapper;
import com.halden.TRPG.service.UserService;

import com.halden.TRPG.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private String staticPath;

    @Value(("${on-path}"))
    private String onPath;


    @Override
    public List<String> getUid(String username) {
        List<UserEntity> list = list(new QueryWrapper<UserEntity>().like("username", username));
        List<String> result = new ArrayList<>();
        for (UserEntity user: list){
            result.add(user.getUid());
        }
        return result;
    }

    @Override
    public String getUsername(String uid) {
        return getById(uid).getUsername();
    }

    @Override
    public CodeEnum saveUser(UserEntity entity) {
        entity.setIsDeleted(false);
        if (getByPhone(entity.getPhone()) != null){
            return CodeEnum.PHONE_HAS_BEEN_REGISTED_EXCEPTHION;
        }
        boolean save = save(entity);
        if (!save){
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("uid", entity.getUid());
        map.put("username", entity.getUsername());
        map.put("token", JwtUtil.createToken(entity.getUid(),entity.getUsername()));
        return CodeEnum.SUCCESS.setData(map);
    }

    @Override
    public UserEntity getByPhone(String phone) {
        UserEntity entity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone));
        return entity;
    }

    @Override
    public CodeEnum logIn(UserEntity entity) {
        UserEntity userEntity = getByPhone(entity.getPhone());
        if (userEntity == null || !userEntity.getPassword().equals(entity.getPassword())){
            return CodeEnum.LOGIN_ACCT_PASSWORD_EXCEPTION;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("uid", userEntity.getUid());
        map.put("username", userEntity.getUsername());
        map.put("token", JwtUtil.createToken(userEntity.getUid(),userEntity.getUsername()));
        return CodeEnum.SUCCESS.setData(map);
    }

    @Override
    public CodeEnum uploadAvatar(MultipartFile avatar, String token) {
        String avatarPath = staticPath + "avatar/";
        String suffix = avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf(".") + 1, avatar.getOriginalFilename().length());
        String uid = JwtUtil.getUid(token);
        String fileName = uid+"."+suffix;
        try {
            File file = new File(avatarPath+fileName);
            if (file.exists()){
                file.delete();
            }
            avatar.transferTo(file);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        String url = onPath+"avatar/"+fileName;
        if (!setAvatar(uid,url)){
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
        return CodeEnum.SUCCESS;
    }

    @Override
    public boolean setAvatar(String uid, String url) {
        UserEntity entity = getById(uid);
        if (entity == null){
            return false;
        }
        entity.setAvatarUrl(url);
        int i = baseMapper.updateById(entity);
        return (i==1);
    }

    @Override
    public CodeEnum getOne(String uid) {
        UserEntity entity = getById(uid);
        if (entity != null){
            return CodeEnum.SUCCESS.setData(entity);
        } else {
            return CodeEnum.NO_SUCH_USER_EXCEPTION;
        }
    }

    @Override
    public CodeEnum updateUser(UserEntity entity) {
        int i = baseMapper.updateById(entity);
        log.info("i="+i);
        if (i == 1){
            return CodeEnum.SUCCESS;
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }

    @Override
    public CodeEnum getAvatar(String uid) {
        UserEntity entity = getById(uid);
        if (entity == null){
            return CodeEnum.NO_SUCH_USER_EXCEPTION;
        }
        String img = entity.getAvatarUrl();
        if (StringUtils.hasLength(img)){
            return CodeEnum.SUCCESS.setData(img);
        } else {
            return CodeEnum.SUCCESS.setData("http://localhost:9001/file/avatar/avatar.jpg");
        }
    }

}
