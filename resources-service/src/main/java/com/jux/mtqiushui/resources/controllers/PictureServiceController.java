package com.jux.mtqiushui.resources.controllers;

import com.jux.mtqiushui.resources.model.Picture;
import com.jux.mtqiushui.resources.services.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "v1/picture")
public class PictureServiceController {

    // 要存放图片的路径
    @Value("${image-path}")
    private String imagePath;

    @Autowired
    private PictureService pictureService;

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> addPicture(@RequestParam("file") MultipartFile file, String width, String height) {
        Map<String, Object> map = new HashMap<>();
        Picture picture = null;
        try {
            picture = pictureService.uploadFile(file, width, height);
        } catch (Exception e) {
           map.put("error", "服务器错误");
           return map;
        }

        if (picture == null) {
            map.put("error", "添加图片失败");
            return map;
        }
        map.put("success",picture);
        return map;
    }
}
