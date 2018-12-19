package com.jux.mtqiushui.resources.services;

import com.jux.mtqiushui.resources.model.NoticeManage;
import com.jux.mtqiushui.resources.model.SearchCondition;
import com.jux.mtqiushui.resources.repository.NoticeManageRepository;
import com.jux.mtqiushui.resources.util.SearchFarmat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Auther: fp
 * @Date: 2018/11/26 08:43
 * @Description:
 */
@Service
public class NoticeManageService {
    @Autowired
    private NoticeManageRepository noticeManageRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 添加通知管理
     * @param noticeManage
     * @return
     * @throws Exception
     */
    @Transactional
    public NoticeManage addNoticeManage(NoticeManage noticeManage) throws Exception {
        NoticeManage save = noticeManageRepository.save(noticeManage);
        return save;
    }

    /**
     * 查询所有通知
     * @param pageable
     * @return
     * @throws Exception
     */
    public Page<NoticeManage> getAllNoticeManage(Pageable pageable) throws Exception {
        Page<NoticeManage> all = noticeManageRepository.findAll(pageable);
        return all;
    }

    /**
     * 根据通知管理ID查询对应的通知信息 并设置为已读
     * @param noticeManageId
     * @return
     * @throws Exception
     */
    @Transactional
    public NoticeManage getNoticeManageById(Long noticeManageId) throws Exception {
        NoticeManage resultNoticeManage = noticeManageRepository.findOne(noticeManageId);
        if (resultNoticeManage != null) {
            if (resultNoticeManage.getIsRead() == 0) {
                resultNoticeManage.setIsRead(1);
                NoticeManage noticeManage = noticeManageRepository.save(resultNoticeManage);
                return noticeManage;
            }
            return resultNoticeManage;
        }

        return resultNoticeManage;
    }

    /**
     * 根据通知管理删除对应的通知信息
     * @param noticeManageId
     * @return
     * @throws Exception
     */
    @Transactional
    public Boolean deleteNoticeManage(Long noticeManageId) throws Exception {
        NoticeManage one = noticeManageRepository.findOne(noticeManageId);
        if (one == null) {
            return false;
        }
        noticeManageRepository.delete(noticeManageId);
        return true;
    }

    /**
     * 修改通知管理
     * @param noticeManageId
     * @param noticeManage
     * @param userName
     * @return
     * @throws Exception
     */
    @Transactional
    public Boolean modifyNoticeManage(Long noticeManageId, NoticeManage noticeManage, String userName) throws Exception {

        NoticeManage one = noticeManageRepository.findOne(noticeManageId);
        System.out.println("one:"+one);
        if (one == null) {
            return false;
        }
        if (noticeManage.getTitle() != null) {
            one.setTitle(noticeManage.getTitle());
        }
        if (noticeManage.getContent() != null) {
            one.setContent(noticeManage.getContent());
        }

        noticeManage.setIsRead(0);
        if (!one.getUserName().equals(userName)) {
            one.setUserName(userName);
        }
        NoticeManage save = noticeManageRepository.save(one);
        System.out.println("save:"+save);
        if (save != null) {
            return true;
        }
        return false;
    }

    /**
     * 多条件查询
     * @param conditionList
     * @return
     * @throws Exception
     */
    public List<NoticeManage> getNoticeManageBySearch(List<SearchCondition> conditionList) throws Exception {

        if (conditionList.size() == 0){
            return null;
        }

        String aNew = " ";
        for (int i = 0; i < conditionList.size(); i++) {

            aNew += SearchFarmat.getNew(conditionList.get(i));

            if (i<conditionList.size() - 1){
                aNew += " and " ;
            }

        }

        String sql = "select id,content,is_read,title,user_name from notice_manage where" + aNew + " order by update_time desc";
        List<NoticeManage> query = jdbcTemplate.query(sql, new RowMapper<NoticeManage>() {
            @Override
            public NoticeManage mapRow(ResultSet resultSet, int i) throws SQLException {
                NoticeManage noticeManage = new NoticeManage();
                noticeManage.setId(resultSet.getLong("id"));
                noticeManage.setTitle(resultSet.getString("title"));
                noticeManage.setContent(resultSet.getString("content"));
                noticeManage.setUserName(resultSet.getString("user_name"));
                noticeManage.setIsRead(resultSet.getInt("is_read"));
                return noticeManage;
            }
        });
        return query;
    }
}
