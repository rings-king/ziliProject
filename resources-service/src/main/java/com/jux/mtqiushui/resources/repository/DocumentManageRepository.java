package com.jux.mtqiushui.resources.repository;

import com.jux.mtqiushui.resources.model.DocumentManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther: fp
 * @Date: 2018/11/26 09:53
 * @Description:
 */
@Repository
public interface DocumentManageRepository extends JpaRepository<DocumentManage,Long>, PagingAndSortingRepository<DocumentManage,Long> {

    public DocumentManage findByFileNameAndSuffix(String fileName,String suffix);

    public List<DocumentManage> findByUserName(String userName);

    public DocumentManage findByFileNameAndUserName(String fileName,String userName);

    public List<DocumentManage> findByFileName(String fileName);
}
