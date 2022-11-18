package com.halden.TRPG.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.entity.RoomMusicEntity;
import com.halden.TRPG.entity.vo.RoomMusicUploadVo;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;


public interface RoomMusicService extends IService<RoomMusicEntity> {
    CodeEnum createMusic(RoomMusicUploadVo vo);

    CodeEnum uploadMusic(MultipartFile music,Long rid, Long id);

    CodeEnum uploadCover(MultipartFile cover, Long id);

    boolean setMusic(String musicSrc, Long id);

    CodeEnum getMusics(Long rid);

    CodeEnum deleteMusic(Long rid, String name);


}
