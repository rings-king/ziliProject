package com.jux.mtqiushui.resources.services;

import com.jux.mtqiushui.resources.model.DocumentManage;
import com.jux.mtqiushui.resources.repository.DocumentManageRepository;
import com.jux.mtqiushui.resources.util.FileNameUtils;
import com.jux.mtqiushui.resources.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.security.Principal;
import java.util.Calendar;
import java.util.List;

/**
 * @Auther: fp
 * @Date: 2018/11/26 09:53
 * @Description:
 */
@Service
public class DocumentManageService {
    @Value("${web.upload-path}")
    //要上传的目标文件存放路径
    private String localPath;
    @Autowired
    private DocumentManageRepository documentManageRepository;

    @Transactional
    public Boolean uploadFile(String fileName, MultipartFile file, Principal user) {

        //将文件信息保存到数据库中
        DocumentManage documentManage = new DocumentManage();
        documentManage.setFileName(fileName);
        documentManage.setUserName(user.getName());
        documentManage.setSuffix(FileNameUtils.getSuffix(file.getOriginalFilename()));
        documentManage.setUpdateTime(Calendar.getInstance().getTime());

        //通过文件名和后缀先判断是否该用户已经上传过该文件
        DocumentManage byFileName = documentManageRepository.findByFileNameAndSuffix(fileName,documentManage.getSuffix());

        if (byFileName == null) { //是新文件
            if (FileUtils.upload(file, localPath, fileName)) {
                //文件上传成功后再将文件信息保存进数据库
                documentManageRepository.save(documentManage);
                return true;
            }
            return false;
        }
        return false;
    }


    /**
     * 获取该用户上传的所有文件
     *
     * @param user
     * @return
     */
    public Page<DocumentManage> getAllDocument(Principal user, Pageable pageable) throws Exception {

        Page<DocumentManage> all = documentManageRepository.findAll(pageable);
        List<DocumentManage> content = all.getContent();

        for (DocumentManage documentManage : content) {
            documentManage.setUrl("resourcesservice" + File.separatorChar + "pdf" + File.separatorChar+documentManage.getFileName() + documentManage.getSuffix());
        }
        System.out.println(":" + all);

        return all;
    }


    /**
     * 删除该用户上传的文件
     * @param id
     * @throws Exception
     */
    @Transactional
    public Boolean deleteDocument(Long id) throws Exception {
        DocumentManage one = documentManageRepository.findOne(id);

        if (one != null) {
            List<DocumentManage> byFileName = documentManageRepository.findByFileName(one.getFileName());

            if (byFileName.size() == 1) {
                File file = new File(localPath + one.getFileName() + one.getSuffix());
                boolean delete = file.delete();
                if (delete){
                    documentManageRepository.delete(id);
                    return true;
                }
            }
            return false;
        }
        return false;

    }


    /**
     * 根据id获取文档
     * @param id
     * @return
     */
    public DocumentManage getDocumentById(Long id)throws Exception{
        DocumentManage one = documentManageRepository.findOne(id);
        one.setUrl("resourcesservice" + File.separatorChar + "pdf" + File.separatorChar+ one.getFileName() + one.getSuffix());
        if (one!=null){
            return one;
        }
        return null;

    }

    /**
     * 修改文档
     * @param fileId
     * @param fileName
     * @param file
     * @return
     */
    @Transactional
    public Boolean updateDocument(Long fileId, String fileName,MultipartFile file)throws Exception {

        //修改两项
        if (fileName != null||file != null){
                //可以修改文件名
                DocumentManage one = documentManageRepository.findOne(fileId);

                String oldFileName = one.getFileName();
                System.out.println("oldFileName ::"+oldFileName);

                //查询原来的文档数量
                List<DocumentManage> byFileName = documentManageRepository.findByFileName(oldFileName);
            if (byFileName.size()==1) {
                File newfile = new File(localPath + oldFileName + one.getSuffix());
                newfile.delete();
            }
                one.setFileName(fileName);
                one.setUpdateTime(Calendar.getInstance().getTime());
                DocumentManage save = documentManageRepository.save(one);
                if (FileUtils.upload(file, localPath, fileName) && save != null) {
                    //删除原文件

                    return true;
                }
            }

        return false;
    }

}
