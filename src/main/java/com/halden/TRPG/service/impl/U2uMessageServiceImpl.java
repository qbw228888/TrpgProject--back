package com.halden.TRPG.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halden.TRPG.entity.U2uMessageEntity;
import com.halden.TRPG.mapper.U2uMessageMapper;
import com.halden.TRPG.service.U2uMessageService;
import org.springframework.stereotype.Service;

@Service
public class U2uMessageServiceImpl extends ServiceImpl<U2uMessageMapper, U2uMessageEntity>
        implements U2uMessageService {
}
