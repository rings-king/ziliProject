package com.jux.mtqiushui.zilidata.service;

import com.jux.mtqiushui.zilidata.model.KanbanPlan;
import com.jux.mtqiushui.zilidata.model.KanbanType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @Auther: fp
 * @Date: 2018/11/29 15:50
 * @Description:
 */
@Service
public class KanbanService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<KanbanType> getAllKanbanType(){

        List<KanbanType> query = jdbcTemplate.query("SELECT [ID]\n" +
          "      ,[CAIZHI]\n" +
          "      ,[ZHUANXING]\n" +
          "      ,[CREATETIME]\n" +
          "      ,[COMMENS]\n" +
          "      ,[STATUS]\n" +
          "  FROM [ZILI_YAJIKANBAN].[dbo].[ZILI_YAJI_KANBAN_TYPE] where DATEDIFF(dd,CREATETIME,getdate()) = 0", new RowMapper<KanbanType>() {
            @Override
            public KanbanType mapRow(ResultSet rs, int rowNum) throws SQLException {
                KanbanType kanbanType = new KanbanType();
                kanbanType.setId(rs.getString("ID"));
                kanbanType.setCaizhi(rs.getString("CAIZHI"));
                kanbanType.setCommens(rs.getString("COMMENS"));
                kanbanType.setCreatTime(rs.getTimestamp("CREATETIME"));
                kanbanType.setZhuangxing(rs.getString("ZHUANXING"));
                kanbanType.setStatus(rs.getString("STATUS"));
                return kanbanType;
            }
        });


        return query;
    }

    public List<KanbanPlan> getAllKanbanPlan(){
        List<KanbanPlan> query = jdbcTemplate.query("SELECT [planno]\n" +
          "      ,[createtime]\n" +
          "      ,[finishdate]\n" +
          "      ,[gongduan]\n" +
          "      ,[liaohao]\n" +
          "      ,[caizhi]\n" +
          "      ,[zhuanxing]\n" +
          "      ,[unit]\n" +
          "      ,[amount]\n" +
          "  FROM [ZILI_YAJIKANBAN].[dbo].[ZILI_YAJI_KANBAN_PLAN] where DATEDIFF(dd,createtime,getdate()) = 0", new RowMapper<KanbanPlan>() {
            @Override
            public KanbanPlan mapRow(ResultSet rs, int rowNum) throws SQLException {
                KanbanPlan kanbanPlan = new KanbanPlan();
                kanbanPlan.setAmount(rs.getString("amount"));
                kanbanPlan.setCaizhi(rs.getString("caizhi"));
                kanbanPlan.setCreatetime(rs.getTimestamp("createtime"));
                kanbanPlan.setFinishdate(rs.getTimestamp("finishdate"));
                kanbanPlan.setGongduan(rs.getString("gongduan"));
                kanbanPlan.setLiaohao(rs.getString("liaohao"));
                kanbanPlan.setZhuanxing(rs.getString("zhuanxing"));
                kanbanPlan.setUnit(rs.getString("unit"));
                kanbanPlan.setPlanno(rs.getString("planno"));
                return kanbanPlan;
            }
        });
        return query;
    }




}
