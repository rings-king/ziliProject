package com.jux.mtqiushui.auth.repository;

import com.jux.mtqiushui.auth.domain.Xtgnb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
//33

public interface XtgnbRepository extends JpaRepository<Xtgnb, Integer> {


    List<Xtgnb> findByGnbm(String s);

    //根据前端返回得menuType 查询返回不同的数据
    @Query(value = "select * from xt_xtgnb where menuList in (:menu)",nativeQuery = true)
    public List<Xtgnb> getAllMessageByMenuType(@Param("menu") List<Integer> menuType);

    //通过权限id集合 获取所有权限
    @Query(value = "select * from xt_xtgnb where id in (:roleList)",nativeQuery = true)
    public List<Xtgnb> getRoleAuthotity(@Param("roleList") List<String> id);

    @Query(value = "select * from xt_xtgnb where sjgnbm is null ",nativeQuery = true)
    public List<Xtgnb> getAllSysterm();


    //根据功能编号 id 和 菜单类别 查询所有功能
    @Query(value = "select * from xt_xtgnb where gnbm like :gn and menuList in (:menu) and id in (:ids)",nativeQuery = true)
    public List<Xtgnb> getByRuleOne(@Param("gn") String gnbm, @Param("menu") List<Integer> menuList,@Param("ids") List<String> idList);

    @Query(value = "select * from xt_xtgnb where gnbm like '?%'",nativeQuery = true)
    public List<Xtgnb> getByRuleTwo(String gnbm);

    @Query(value = "select gnmc from xt_xtgnb where id in (:roleList)",nativeQuery = true)
    public List<String> getGnmcMessageById(@Param("roleList")List<String> id);


    @Query(value = "select * from xt_xtgnb where gnbm in (:gnbmlist)",nativeQuery = true)
    public List<Xtgnb> findByGnbm(@Param("gnbmlist") List<String> list);

    @Query(value = "select id from xt_xtgnb",nativeQuery = true)
    public List<Long> getAllId();

}
