package com.halden.TRPG.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class FileUtils {

    public static void delFile(File index){
        if (index.isDirectory()){
            File[] files = index.listFiles();
            for (File in: files) {
                delFile(in);
            }
        }
        index.delete();
        //出现几次删除成功代表有几个文件和文本文件
        log.info("删除成功");
    }

}
