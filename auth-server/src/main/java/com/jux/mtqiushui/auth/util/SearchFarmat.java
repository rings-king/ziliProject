package com.jux.mtqiushui.auth.util;

import com.jux.mtqiushui.auth.domain.Vo.SearchCondition;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchFarmat {

    public static String getNew(SearchCondition condition){
        //获得逻辑符
        String sql = null;
        switch(condition.getOperator()){
            case "<>":
                sql= condition.getValue() instanceof String ? condition.getFieldname()+"<>"+"'"+condition.getValue()+"'": condition.getFieldname()+"<>"+condition.getValue();
                break;
            case "=":
                sql= condition.getValue() instanceof String ? condition.getFieldname()+"="+"'"+condition.getValue()+"'": condition.getFieldname()+"="+condition.getValue();
                break;
            case "is null":sql=condition.getFieldname()+" is null";break;
            case "is not null":sql=condition.getFieldname()+" is not null";break;
            case "like |%":sql=condition.getFieldname()+" like "+"'"+condition.getValue()+"%"+"'";break;
            case "like %|":sql=condition.getFieldname()+" like "+"'"+"%"+condition.getValue()+"'";break;
            case "like %|%":sql=condition.getFieldname()+" like "+"'"+"%"+condition.getValue()+"%"+"'";break;
            case "not like %|%":sql=condition.getFieldname()+" not like "+"'"+"%"+condition.getValue()+"%"+"'";break;
            case "before":sql=condition.getFieldname()+" < "+ "'"+condition.getValue()+"'";break;
            case "after":sql=condition.getFieldname()+" > "+ "'"+condition.getValue()+"'";break;
            case "<7d":
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse((String) condition.getValue());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, -7);
                    sql=condition.getFieldname()+" < " + "'" + simpleDateFormat.format(calendar.getTime()) + "'";
                } catch (ParseException e) {
                    e.printStackTrace();
                }break;
            case "<15d":
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse((String) condition.getValue());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, -15);
                    sql=condition.getFieldname()+" < " + "'" + simpleDateFormat.format(calendar.getTime()) + "'";
                } catch (ParseException e) {
                    e.printStackTrace();
                }break;
            case "<1m":
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse((String) condition.getValue());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.MONTH, -1);
                    sql=condition.getFieldname()+" < " + "'" + simpleDateFormat.format(calendar.getTime()) + "'";
                } catch (ParseException e) {
                    e.printStackTrace();
                }break;
            case "<3m":
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse((String) condition.getValue());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.MONTH, -3);
                    sql=condition.getFieldname()+" < " + "'" + simpleDateFormat.format(calendar.getTime()) + "'";
                } catch (ParseException e) {
                    e.printStackTrace();
                }break;
            case "<6m":
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse((String) condition.getValue());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.MONTH, -6);
                    sql=condition.getFieldname()+" < " + "'" + simpleDateFormat.format(calendar.getTime()) + "'";
                } catch (ParseException e) {
                    e.printStackTrace();
                }break;
            case "<1y":
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = simpleDateFormat.parse((String) condition.getValue());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.YEAR, -1);
                    sql=condition.getFieldname()+" < " + "'" + simpleDateFormat.format(calendar.getTime()) + "'";
                } catch (ParseException e) {
                    e.printStackTrace();
                }break;
            default :sql="1=1";break;
        }
        return sql;

    }

    public static List<SearchCondition> formatValue(List<SearchCondition> searchConditions) {
        for (SearchCondition searchCondition : searchConditions) {
            Object value = searchCondition.getValue();
            if (value instanceof String) {
                value = (String) value;
                try {
                    byte[] bytes = ((String) value).getBytes("UTF-8");
                    String newValue = new String(bytes,"UTF-8");
                    value = newValue;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return searchConditions;
    }

}
