package com.halden.TRPG.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halden.TRPG.common.CodeEnum;
import com.halden.TRPG.entity.RoomMusicEntity;
import com.halden.TRPG.entity.vo.RoomMusicResponseVo;
import com.halden.TRPG.entity.vo.RoomMusicUploadVo;
import com.halden.TRPG.mapper.RoomMusicMapper;
import com.halden.TRPG.service.RoomMusicService;
import com.halden.TRPG.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@Transactional
public class RoomMusicServiceImpl extends ServiceImpl<RoomMusicMapper, RoomMusicEntity> implements RoomMusicService {

    @Autowired
    private String staticPath;


    @Value(("${on-path}"))
    private String onPath;

    @Override
    public CodeEnum createMusic(RoomMusicUploadVo vo) {
        RoomMusicEntity one = getOne(new QueryWrapper<RoomMusicEntity>().eq("rid", vo.getRid()).eq("name", vo.getName()));
        if (one != null){
            return CodeEnum.ALREADY_EXIST_EXCEPTION;
        }
        RoomMusicEntity entity = new RoomMusicEntity();
        BeanUtils.copyProperties(vo, entity);
        File dir = new File(staticPath+"room/"+vo.getRid()+"/music/"+vo.getName()+"/");
        if (!dir.exists()){
            dir.mkdirs();
        }
        boolean save = save(entity);
        if (save) {
            return CodeEnum.SUCCESS.setData(entity.getId());
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }

    @Override
    public CodeEnum uploadMusic(MultipartFile music, Long rid, Long id) {
        RoomMusicEntity entity = getById(id);
        String fileName = music.getOriginalFilename();
        try {
            String dirPath = staticPath+"room/"+rid+"/music/"+entity.getName()+"/";
            File file = new File(dirPath + fileName);
            if (file.exists()){
                file.delete();
                music.transferTo(file);
                String url = onPath+"room/"+rid+"/image/"+fileName;
                return CodeEnum.SUCCESS.setData(url);
            }
            music.transferTo(file);
        } catch (Exception e){
            log.error(e.toString());
            removeById(id);
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
        String url = onPath+"room/"+rid+"/music/"+entity.getName()+"/"+fileName;
        boolean setMusic = setMusic(url, id);
        if (setMusic == false){
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
        return CodeEnum.SUCCESS.setData(url);
    }

    @Override
    public CodeEnum uploadCover(MultipartFile cover, Long id) {
        RoomMusicEntity entity = getById(id);
        String fileName = cover.getOriginalFilename();
        String path = entity.getMusic().replaceAll(onPath,"");
        String[] arr = path.split("/");
        path = path.replaceAll(arr[arr.length-1],"");
        path = path + fileName;
        log.info(path);
        File file = new File(staticPath+path);
        try {
            cover.transferTo(file);
        } catch (IOException e) {
            log.error(e.toString());
            removeById(id);
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
        String url = onPath + path;
        entity.setCover(url);
        boolean update = updateById(entity);
        if (update){
            return CodeEnum.SUCCESS.setData(entity.getCover());
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }

    @Override
    public boolean setMusic(String musicSrc, Long id) {
        RoomMusicEntity entity = getById(id);
        entity.setMusic(musicSrc);
        boolean update = updateById(entity);
        return update;
    }

    @Override
    public CodeEnum getMusics(Long rid) {
        List<RoomMusicEntity> entities = list(new QueryWrapper<RoomMusicEntity>().eq("rid", rid));
        if (entities == null || entities.size() == 0){
            return CodeEnum.SUCCESS;
        }
        List<RoomMusicResponseVo> musics = new ArrayList<>();
        for (RoomMusicEntity entity : entities){
            RoomMusicResponseVo vo = new RoomMusicResponseVo();
            BeanUtils.copyProperties(entity,vo);
            vo.setUrl(entity.getMusic());
            musics.add(vo);
        }
        return CodeEnum.SUCCESS.setData(musics);
    }

    @Override
    public CodeEnum deleteMusic(Long rid, String name) {
        String dirPath = staticPath+"room/"+rid+"/music/"+name;
        FileUtils.delFile(new File(dirPath));
        boolean remove = remove(new QueryWrapper<RoomMusicEntity>()
                .eq("rid", rid).eq("name", name));
        if (remove) {
            return CodeEnum.SUCCESS;
        } else {
            return CodeEnum.OPTION_FAIL_EXCEPTION;
        }
    }
}
