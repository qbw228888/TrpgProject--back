package com.halden.TRPG.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.entity.RoomImageEntity;
import com.halden.TRPG.mapper.RoomImageMapper;
import com.halden.TRPG.service.RoomImageService;
import com.halden.TRPG.service.RoomMusicService;
import com.halden.TRPG.utils.FileUtils;
import com.halden.TRPG.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@Slf4j
public class RoomImageServiceImpl extends ServiceImpl<RoomImageMapper, RoomImageEntity> implements RoomImageService {

    @Autowired
    private String staticPath;

    @Value(("${on-path}"))
    private String onPath;


    @Override
    public CodeEnum uploadImage(MultipartFile image, Long rid) {
        String ridStr = rid.toString();
        String fileName = image.getOriginalFilename();
        try {
            File dir = new File(staticPath+"room/"+ridStr+"/image");
            if (!dir.exists()){
                dir.mkdirs();
            }
            File file = new File(dir.getPath()+"/"+fileName);
            if (file.exists()){
                file.delete();
                image.transferTo(file);
                String url = onPath+"room/"+rid+"/image/"+fileName;
                return CodeEnum.SUCCESS.setData(url);
            }
            image.transferTo(file);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
        String url = onPath+"room/"+rid+"/image/"+fileName;
        if (!setImg(rid, url)){
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
        return CodeEnum.SUCCESS.setData(url);
    }

    @Override
    public boolean setImg(Long rid, String imageSrc) {
        RoomImageEntity entity = new RoomImageEntity();
        entity.setImg(imageSrc);
        entity.setRid(rid);
        boolean b = save(entity);
        return b;
    }

    @Override
    public CodeEnum getImages(Long rid) {
        List<RoomImageEntity> entities = baseMapper.selectList(new QueryWrapper<RoomImageEntity>().eq("rid", rid));
        if (entities == null || entities.size() == 0){
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
        List<String> images = new ArrayList<>();
        for (RoomImageEntity entity : entities){
            images.add(entity.getImg());
        }
        return CodeEnum.SUCCESS.setData(images);
    }

    @Override
    public CodeEnum deleteImage(Long rid, String url) {
        url = url.replaceAll("%3A", ":").replaceAll("%2F", "/")  //过滤URL 包含中文
                .replaceAll("=","");
        int delete = baseMapper.delete(new QueryWrapper<RoomImageEntity>()
                .eq("rid", rid)
                .eq("img", url));
        if (delete != 1){
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
        url = url.replaceAll(onPath,"");
        String path = staticPath + url;
        File file = new File(path);
        if (file.exists()){
            file.delete();
        }
        return CodeEnum.SUCCESS;
    }
}
