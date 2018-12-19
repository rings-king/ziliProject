package com.jux.mtqiushui.resources.util;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传工具包
 */
@Transactional
public class FileUtils {

    /**
     *
     * @param file 文件
     * @param path 文件存放路径
     * @param fileName 新文件名
     * @return
     */

    public static boolean upload(MultipartFile file, String path, String fileName){
        // 生成新的文件名
        String realPath = path + System.getProperty("file.separator") + FileNameUtils.getFileName(file.getOriginalFilename(),fileName);

        //使用原文件名
        //String realPath = path + "/" + fileName;

        File dest = new File(realPath);
        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 保存图片到服务器上
     * @param file
     * @param path
     * @param fileName
     * @return
     */
    public static boolean saveImage(MultipartFile file, String path, String fileName){
        // 生成新的文件名
        String realPath = path + System.getProperty("file.separator") + fileName;

        //使用原文件名
        //String realPath = path + "/" + fileName;

        File dest = new File(realPath);
        //判断文件父目录是否存在
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }

        try {
            //保存文件
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }
}
