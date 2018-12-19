package com.jux.mtqiushui.resources.controllers;

import com.jux.mtqiushui.resources.model.DocumentManage;
import com.jux.mtqiushui.resources.services.DocumentManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: fp
 * @Date: 2018/11/26 09:29
 * @Description:
 */
@RestController
@RequestMapping(value = "v1/documentManage")
public class DocumentManageServiceController {
    @Autowired
    private DocumentManageService documentManageService;

    @RequestMapping(method = RequestMethod.POST)
    /**
     * 上传文件
     * fileName : 新文件名不带后缀
     */
    public Map<String, Object> uploadDocument(String fileName, @RequestParam("file") MultipartFile file, Principal user) {
        Map<String, Object> map = new HashMap<>();
        Boolean aBoolean = documentManageService.uploadFile(fileName, file, user);
        if (aBoolean) {
            map.put("success", "文件上传成功");
        } else {
            map.put("error", "文件上传失败请检查文件是否重复");
        }

        return map;
    }

    /**
     * 获取该用户上传的所有文件
     *
     * @param user
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Object getAllDocument(Principal user, @PageableDefault(value = 20, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<DocumentManage> documentManages;
        Map<String, Object> map = new HashMap<>();
        try {
            documentManages = documentManageService.getAllDocument(user, pageable);
            return documentManages;
        } catch (Exception e) {
            map.put("error","服务器错误");
            return map;
        }

    }

    /**
     * 根据id获取文档
     * @param id
     * @return
     */
    @RequestMapping(value = "/{documentId}", method = RequestMethod.GET)
    public Object getDocumentBYId(@PathVariable("documentId") Long id) {
        Map<String, Object> map = new HashMap<>();

        DocumentManage documentById = null;
        try {
            documentById = documentManageService.getDocumentById(id);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("error","服务器错误");
        }
        return documentById;
    }


    /**
     * 删除该用户上传的所有文件
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{documentId}", method = RequestMethod.DELETE)
    public Map<String, Object> deleteDocument(@PathVariable("documentId") Long id) {
        Map<String, Object> map = new HashMap<>();
            try {
                Boolean aBoolean = documentManageService.deleteDocument(id);
                if (aBoolean){
                    map.put("success", "true");
                    return map;
                }
                map.put("error","删除失败");
                return map;

        } catch (Exception e) {
            System.out.println("errorMessage" + e.getMessage());
            map.put("error", "服务器异常");
            return map;
        }
    }


    /**
     * 文档修改
     *
     * @param fileId
     * @param fileName
     * @param file
     * @return
     */
    @RequestMapping(value = "/{fileId}", method = RequestMethod.POST)
    public Map<String, Object> updateDocument(@PathVariable("fileId") Long fileId, String fileName, MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        try {
            Boolean aBoolean = documentManageService.updateDocument(Long.valueOf(fileId), fileName, file);
            if (aBoolean) {
                map.put("success", "修改成功");
                return map;
            }
        } catch (Exception e) {
            map.put("error", "修改失败服务器错误");
            return map;
        }

        map.put("error", "修改失败");
        return map;
    }




}
