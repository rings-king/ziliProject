package com.jux.mtqiushui.resources.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class FileNameUtils {

    /**
     * 获取文件后缀
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName,String fileName){
        return fileName + FileNameUtils.getSuffix(fileOriginName);
    }

    /**
     * 为图片生成新名字
     * @param file
     * @return
     */
    public static String newFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        String newFileName = new Date().getTime() + suffix;
        return newFileName;
    }

}
