package com.halden.TRPG.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.halden.TRPG.entity.MessageEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<MessageEntity> {
}
