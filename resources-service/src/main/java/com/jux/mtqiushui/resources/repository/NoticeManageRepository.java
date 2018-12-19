package com.jux.mtqiushui.resources.repository;

import com.jux.mtqiushui.resources.model.NoticeManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @Auther: fp
 * @Date: 2018/11/26 08:44
 * @Description:
 */
@Repository
public interface NoticeManageRepository extends JpaRepository<NoticeManage,Long> , PagingAndSortingRepository<NoticeManage,Long> {
}
