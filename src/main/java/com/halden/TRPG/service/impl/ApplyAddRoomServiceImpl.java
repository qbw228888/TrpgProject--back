package com.halden.TRPG.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halden.TRPG.entity.ApplyAddRoomEntity;
import com.halden.TRPG.mapper.ApplyAddRoomMapper;
import com.halden.TRPG.service.ApplyAddRoomService;
import org.springframework.stereotype.Service;

@Service
public class ApplyAddRoomServiceImpl extends ServiceImpl<ApplyAddRoomMapper, ApplyAddRoomEntity>
        implements ApplyAddRoomService {
    @Override
    public boolean apply(Long rid, String uid) {
        ApplyAddRoomEntity entity = new ApplyAddRoomEntity();
        entity.setApplierUid(uid);
        entity.setRid(rid);
        return save(entity);
    }

    @Override
    public boolean ifApplied(Long rid, String uid) {
        ApplyAddRoomEntity entity = baseMapper.selectOne(new QueryWrapper<ApplyAddRoomEntity>()
                .eq("rid", rid)
                .eq("applier_uid", uid));
        if (entity == null){
            return false;
        } else {
            return true;
        }
    }
}
