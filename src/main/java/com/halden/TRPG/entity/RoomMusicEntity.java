package com.halden.TRPG.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("room_music")
public class RoomMusicEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String music;

    private Long rid;

    private String name;

    private String artist;

    private String cover;

    private String lrc;
}
