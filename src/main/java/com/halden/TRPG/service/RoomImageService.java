package com.halden.TRPG.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.entity.RoomImageEntity;
import org.springframework.web.multipart.MultipartFile;


public interface RoomImageService extends IService<RoomImageEntity> {
    CodeEnum uploadImage(MultipartFile image, Long rid);

    boolean setImg(Long rid, String imageSrc);

    CodeEnum getImages(Long rid);

    CodeEnum deleteImage(Long rid, String url);
}
