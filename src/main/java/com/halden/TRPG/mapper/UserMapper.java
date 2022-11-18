package com.halden.TRPG.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.halden.TRPG.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {
}
