package com.jux.mtqiushui.resources.services;

import com.jux.mtqiushui.resources.model.Picture;
import com.jux.mtqiushui.resources.repository.PictureRepository;
import com.jux.mtqiushui.resources.util.FileNameUtils;
import com.jux.mtqiushui.resources.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class PictureService {

    // 要存放图片的路径
    @Value("${image-path}")
    private String imagePath;

    @Autowired
    private PictureRepository pictureRepository;

    /**
     * 上传图片
     * @param file
     * @param width
     * @param height
     * @return
     * @throws Exception
     */
    @Transactional
    public Picture uploadFile(MultipartFile file, String width, String height) throws Exception{
        Picture picture = new Picture();
        picture.setPictureHeight(height);
        picture.setPictureWidth(width);
        String newFileName = FileNameUtils.newFileName(file);
        picture.setPictureName(newFileName);
        String realPath = "resourcesservice" + File.separatorChar + "images" + File.separatorChar + newFileName;
        picture.setPictureUrl(realPath);
        Picture save = pictureRepository.save(picture);
        if (save == null) {
            return null;
        }
        FileUtils.saveImage(file, imagePath, newFileName);
        return save ;
    }
}
