package com.halden.TRPG.entity.vo;

import lombok.Data;

@Data
public class RoomMusicResponseVo {
    private String name;
    private String artist;
    private String url;
    private String cover;
    private String lrc = null;
    private String theme = null;
}
