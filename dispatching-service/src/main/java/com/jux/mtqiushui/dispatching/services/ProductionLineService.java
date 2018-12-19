package com.jux.mtqiushui.dispatching.services;

import com.jux.mtqiushui.dispatching.model.ProductionLine;
import com.jux.mtqiushui.dispatching.model.SearchCondition;
import com.jux.mtqiushui.dispatching.model.Station;
import com.jux.mtqiushui.dispatching.repository.ProductionLineRepository;
import com.jux.mtqiushui.dispatching.repository.StationRepository;
import com.jux.mtqiushui.dispatching.util.ObjectIsNull;
import com.jux.mtqiushui.dispatching.util.SearchFarmat;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class ProductionLineService {

    @Autowired
    private ProductionLineRepository productionLineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //添加产线
    @Transactional
    public ProductionLine addProductionLine(ProductionLine productionLine) throws Exception {
        Set<Station> productionLineStationTabs = productionLine.getProductionLineStationTab1();
        if (productionLineStationTabs != null) {
            for (Station station : productionLineStationTabs) {
                //将工位与产线关联
                station.setProductionLine(productionLine);
            }
        }
        productionLine.setUpdateTime(Calendar.getInstance().getTime());
        ProductionLine save = productionLineRepository.save(productionLine);
        return save;
    }

    //查询所有产线以及附带的工位
    public Page<ProductionLine> findAllProductionLine(Pageable pageable) throws Exception {
        Page<ProductionLine> all = productionLineRepository.findAll(pageable);
        return all;
    }

    //根据id查询对应的产线以及附带的工位
    public ProductionLine findProductionLineById(Long id) throws Exception {
        ProductionLine productionLingById = productionLineRepository.findProductionLingById(id);
        return productionLingById;
    }

    //根据id删除对应产线以及附带工位
    @Transactional
    public void deleteProductionLineById(Long id) throws Exception {
        productionLineRepository.delete(id);
    }

    //修改产线定义以及子表工位的增删改
    public Boolean modifyProductionLines(List<ProductionLine> productionLines, ProductionLine productionLine) throws Exception {
        //获得添加的工位
        ProductionLine addnew = productionLines.get(0);
        Boolean addproductionLine = addProductionLineAndStations(addnew, productionLine);
        //获得要删除的工位
        ProductionLine remove = productionLines.get(1);
        Boolean removeProductionLine = removeProductionLineAndStations(remove, productionLine);
        //获得要修改的工位
        ProductionLine update = productionLines.get(2);
        Boolean updateProductionLine = updateProductionLineAndStations(update, productionLine);
        if (addproductionLine == true && removeProductionLine == true && updateProductionLine == true) {
            return true;
        }
        return false;
    }

    //修改产线及其附带工位
    private Boolean updateProductionLineAndStations(ProductionLine updateproductionLine, ProductionLine productionLine) throws Exception {
        Boolean remove = ObjectIsNull.isAllFieldNull(updateproductionLine);
        if (remove == false) {
            if (updateproductionLine.getProductionLineCode() != null) {
                productionLine.setProductionLineCode(updateproductionLine.getProductionLineCode());
            }
            if (updateproductionLine.getProductionLineName() != null) {
                productionLine.setProductionLineName(updateproductionLine.getProductionLineName());
            }
            if (updateproductionLine.getProductionLinePrinciple() != null) {
                productionLine.setProductionLinePrinciple(updateproductionLine.getProductionLinePrinciple());
            }
            //设置修改时间
            productionLine.setUpdateTime(Calendar.getInstance().getTime());
            //获得要修改的工位集合
            Set<Station> productionLineStationTab1 = updateproductionLine.getProductionLineStationTab1();
            if (productionLineStationTab1.size() != 0) {
                Set<Station> productionLineStationTabs = productionLine.getProductionLineStationTab1();
                for (Station station : productionLineStationTab1) {
                    for (Station stationYuan : productionLineStationTabs) {
                        if (stationYuan.getId().longValue() == station.getId().longValue()) {
                            if (station.getStationNum() != null) {
                                stationYuan.setStationNum(station.getStationNum());
                            }
                            if (station.getStationName() != null) {
                                stationYuan.setStationName(station.getStationName());
                            }
                            break;
                        }
                    }
                }
            }
        }
        ProductionLine save = productionLineRepository.save(productionLine);
        return save != null ? true : false;
    }

    //删除产线子工位
    private Boolean removeProductionLineAndStations(ProductionLine removeproductionLine, ProductionLine productionLine) throws Exception {
        Boolean remove = ObjectIsNull.isAllFieldNull(removeproductionLine);
        if (remove == false) {
            //遍历要删除的工位集合
            Set<Station> productionLineStationTabl = removeproductionLine.getProductionLineStationTab1();
            //判断要删除的工位集合是否为空
            if (productionLineStationTabl.size() != 0) {
                //获得数据库已经存在的工位集合
                Set<Station> productionLineStationTabs = productionLine.getProductionLineStationTab1();
                //创建一个迭代器存储数据库的工位集合
                Iterator<Station> iterator = productionLineStationTabs.iterator();
                //遍历要删除的工位
                for (Station station : productionLineStationTabl) {
                    System.out.println("************ID" + station.getId());
                    while (iterator.hasNext()) {
                        //获得数据库每一个工位对象
                        Station next = iterator.next();
                        //如果id相同
                        if (next.getId() == station.getId()) {
                            //删除工位
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
        }
        ProductionLine save = productionLineRepository.save(productionLine);
        return save != null ? true : false;
    }

    //添加产线子工位
    private Boolean addProductionLineAndStations(ProductionLine addproductionLine, ProductionLine productionLine) throws Exception {
        //解析要添加的工位
        Boolean addnew = ObjectIsNull.isAllFieldNull(addproductionLine);
        if (addnew == false) {
            //遍历要添加的工位集合
            Set<Station> productionLineStationTab1 = addproductionLine.getProductionLineStationTab1();
            if (productionLineStationTab1.size() != 0) {
                Set<Station> productionLineStationTabs = productionLine.getProductionLineStationTab1();
                //遍历要添加的每一个工位
                for (Station station : productionLineStationTab1) {
                    //将该工位与当前的产线关联
                    station.setProductionLine(productionLine);
                    //将该工位添加到原数据库集合-->保存
                    productionLineStationTabs.add(station);
                }
            }
        }
        ProductionLine save = productionLineRepository.save(productionLine);
        return save != null ? true : false;
    }

    //根据产线id/工位id查询对应的产线名称以及工位名称
    public Map<String, String> getProductionLineNameAndStationName(Long productionLineId, Long stationId) throws Exception {
        Map<String, String> map = new HashMap<>();
        try {
            if (productionLineId!=null) {
                ProductionLine productionLine = findProductionLineById(productionLineId);
                if (productionLine != null) {
                    map.put("productionLineName", productionLine.getProductionLineName());
                }else {
                    return null;
                }
            }
            if (stationId == null) {
                    return  map;
            } else {
                Station station = findStationById(stationId);
                if (station == null) {
                    return null;
                } else {
                    map.put("stationName", station.getStationName());
                }
            }
        } catch (Exception e) {
            return null;
        }
        return map;
    }

    //根据id查询工位
    public Station findStationById(Long stationId) {
        return stationRepository.findOne(stationId);
    }

    /**
     * 搜索
     *
     * @param conditionList
     * @return
     */
    public List<ProductionLine> findProductionLine(List<SearchCondition> conditionList) throws Exception {
        //迭代  给每个字段取别名
        Iterator<SearchCondition> iterator = conditionList.iterator();
        while (iterator.hasNext()) {
            SearchCondition next = iterator.next();
            //主表
            if (next.getTablename().equals("Mes_ProductionLine_Mes_ProductionLinesStore")) {
                next.setFieldname("p." + next.getFieldname());
                continue;
            }
            //子表
            if (next.getTablename().equals("Mes_ProductionLine_productionLineStationTab1sStore")) {
                next.setFieldname("s." + next.getFieldname());
                continue;
            }
        }
        String aNew = " ";
        String nNew = "";
        List<SearchCondition> stations = new ArrayList<>();
        //遍历集合对象
        for (int i = 0; i < conditionList.size(); i++) {
            //判断传过来的是否是去查询子表的字段
            aNew = aNew + SearchFarmat.getNew(conditionList.get(i));
            //判断如果子表有条件就添加另一个集合
            if (conditionList.get(i).getTablename().equals("Mes_ProductionLine_productionLineStationTab1sStore")) {
                stations.add(conditionList.get(i));
            }
            System.out.println("SQL语句" + aNew);
            if (i < conditionList.size() - 1) {
                aNew += " and ";
            }
        }
        if (stations.size() != 0) {
            for (int i = 0; i < stations.size(); i++) {
                nNew = nNew + SearchFarmat.getNew(stations.get(i));
                if (i < stations.size() - 1) {
                    nNew += " and ";
                }
            }
        }

        //判断sql中是否包含表名与名称字段
        if (aNew.contains("productionLineName")) {
            aNew = aNew.replace("productionLineName", "production_line_name");
        }
        if (aNew.contains("productionLineCode")) {
            aNew = aNew.replace("productionLineCode", "production_line_code");
        }
        if (aNew.contains("productionLinePrinciple")) {
            aNew = aNew.replace("productionLinePrinciple", "production_line_principle");
        }
        //判断sql中是否包含子表与名称字段
        //工位名称
        if (aNew.contains("stationName")) {
            aNew = aNew.replace("stationName", "station_name");
        }
        //工位序号
        if (aNew.contains("stationNum")) {
            aNew = aNew.replace("stationNum", "station_num");
        }
        if (nNew.contains("stationName")) {
            nNew = nNew.replace("stationName", "station_name");
        }
        //工位序号
        if (nNew.contains("stationNum")) {
            nNew = nNew.replace("stationNum", "station_num");
        }
        System.out.println("转换过的SQL" + aNew);
        String sql = "select DISTINCT p.id,p.production_line_name,p.production_line_code,p.production_line_principle,p.update_time,s.id,s.station_num,s.station_name from production_line as p,station as s where p.id = s.production_line_id and" + aNew + " order by update_time desc";
        System.out.println("拼接的主表SQL" + sql);
        List<ProductionLine> query = jdbcTemplate.query(sql, new RowMapper<ProductionLine>() {
            public ProductionLine mapRow(ResultSet resultSet, int i) throws SQLException {
                ProductionLine productionLine = new ProductionLine();
                productionLine.setId(resultSet.getLong("id"));
                productionLine.setProductionLineName(resultSet.getString("production_line_name"));
                productionLine.setProductionLineCode(resultSet.getString("production_line_code"));
                productionLine.setProductionLinePrinciple(resultSet.getString("production_line_principle"));
                return productionLine;
            }
        });

        for (ProductionLine productionLine : query) {
            //获得主表id
            Long id = productionLine.getId();
            String sqlchirden = "";
            if (StringUtils.isEmpty(nNew)) {
                sqlchirden = "select s.id,s.station_name,s.station_num from station as s where s.production_line_id = +" + id;
            } else {
                //子表查询
                sqlchirden = "select s.id,s.station_name,s.station_num from station as s where  s.production_line_id = " + id + "  and " + nNew;
            }
            System.out.println("拼接的子表SQL" + sqlchirden);
            List<Station> query1 = jdbcTemplate.query(sqlchirden, new RowMapper<Station>() {
                @Override
                public Station mapRow(ResultSet resultSet, int i) throws SQLException {
                    Station station = new Station();
                    station.setId(resultSet.getLong("id"));
                    station.setStationName(resultSet.getString("station_name"));
                    station.setStationNum(resultSet.getString("station_num"));
                    return station;
                }
            });
            System.out.println("子表查询出来的字段" + query1.size());
            //比较器
            Set<Station> set = new TreeSet<>(new Comparator<Station>() {
                @Override
                public int compare(Station station, Station t1) {
                    return station.getId().intValue() - t1.getId().intValue();
                }
            });
            //将查询出来的子表对象放入集合当中排序
            set.addAll(query1);
            // productionLine.setProductionLineStationTab1(set);
        }
        System.out.println("主表数量" + query.size());
        for (int i = 0; i < query.size() - 1; i++) {
            if (query.get(i).getId().longValue() == query.get(i + 1).getId().longValue()) {
                query.remove(i);
                i = i - 1;
            }
        }
        return query;
    }
}

