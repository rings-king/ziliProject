package com.jux.mtqiushui.auth.service;

import com.jux.mtqiushui.auth.domain.Vo.XtgnbBo;
import com.jux.mtqiushui.auth.domain.Vo.XtgnbBo01;
import com.jux.mtqiushui.auth.domain.Vo.XtgnbBo02;
import com.jux.mtqiushui.auth.domain.Vo.XtgnbVo;
import com.jux.mtqiushui.auth.domain.Xtgnb;
import com.jux.mtqiushui.auth.repository.XtgnbRepository;
import com.jux.mtqiushui.auth.util.Base64ImageUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

//01

@Service
@Transactional
public class XtgnbService {
    @Autowired
    private XtgnbRepository xtgnbRepository;

    /**
     *获取所有权限
     * @param list  功能表菜单id集合
     * @return
     */
    public Object getAllgnbm(List<Integer> list){
        //根节点
        List<XtgnbVo> rootMenu = new ArrayList<>();
       // List<XtgnbVo> topGnbs = new ArrayList<>();
        List<Xtgnb> topgnbs = null;

        //如果菜单为空 调用查询全部方法
        if (list.size() == 0 || list==null){
            topgnbs  = xtgnbRepository.findAll();
        }else {
             topgnbs = xtgnbRepository.getAllMessageByMenuType(list);
        }
        rootMenu = getXtgnbByxtblist(topgnbs);


        return rootMenu;
    }

    /**
     * 通过功能列表 进行树状结构组装
     * @param topgnbs
     * @return
     */
    public  List<XtgnbVo> getXtgnbByxtblist(List<Xtgnb> topgnbs){
        List<XtgnbVo> rootMenu = new ArrayList<>();
        List<XtgnbVo> topGnbs = new ArrayList<>();
        for (Xtgnb topgnb : topgnbs) {
            //包装有用的属性
            XtgnbVo xtgnbVo = new XtgnbVo();
            xtgnbVo.setId(topgnb.getId());
            xtgnbVo.setMenutype(topgnb.getMenuType());
            xtgnbVo.setGnbm(topgnb.getGnbm());
            xtgnbVo.setName(topgnb.getGnsy());
            xtgnbVo.setText(topgnb.getGnmc());
            xtgnbVo.setLeaf(topgnb.getMjbz());
            xtgnbVo.setRegiontype(topgnb.getRegiontype());
            xtgnbVo.setSjgbnm(topgnb.getSjgnbm());
            if (topgnb.getSjgnbm()==null){
                rootMenu.add(xtgnbVo);
           }
            topGnbs.add(xtgnbVo);
        }

        //遍历为根节点设置子节点列表
        for (XtgnbVo xtgnbVo : rootMenu) {

            //获取根节点下所有字节点
            List<XtgnbVo> childCategory = findChildCategory(topGnbs, xtgnbVo.getGnbm());
            //给根节点设置子节点
            xtgnbVo.setChildren(childCategory);
        }

        System.out.println("----11-------------"+rootMenu);
        return rootMenu;
    }


    //递归查找子功能列表
    public  List<XtgnbVo> findChildCategory(List<XtgnbVo> topGnbs,String gnbm){
        List<XtgnbVo> childList = new ArrayList<>();
        for (XtgnbVo xtgnbVo : topGnbs) {
            //遍历所有节点，将父列表id和传过来的id比较
            if (StringUtils.isNotBlank(xtgnbVo.getSjgbnm())){
                if (xtgnbVo.getSjgbnm().equals(gnbm)){
                    System.out.println("****************************"+gnbm);
                    childList.add(xtgnbVo);
                }
            }
        }
        //遍历子功能列表
        for (XtgnbVo xtgnbVo : childList) {
            xtgnbVo.setChildren(findChildCategory(topGnbs,xtgnbVo.getGnbm()));
        }
        if (childList.size() == 0){
            return new ArrayList<>();

        }
        return childList;
    }


    /**
     * 根据当前功能编码获取其下包括自己的所有子结构树
     * @param xtgnbs
     * @param gnbm
     * @return
     */
    public List<XtgnbVo> getXtmnbTree(List<Xtgnb> xtgnbs,String gnbm) {

        List<XtgnbVo> rootMenu = new ArrayList<>();

        List<XtgnbVo> topGnbs = new ArrayList<>();
        for (Xtgnb topgnb : xtgnbs) {
            //包装有用的属性
            XtgnbVo xtgnbVo = new XtgnbVo();
            xtgnbVo.setId(topgnb.getId());
            xtgnbVo.setMenutype(topgnb.getMenuType());
            xtgnbVo.setGnbm(topgnb.getGnbm());
            xtgnbVo.setName(topgnb.getGnsy());
            xtgnbVo.setText(topgnb.getGnmc());
            xtgnbVo.setLeaf(topgnb.getMjbz());
            xtgnbVo.setRegiontype(topgnb.getRegiontype());
            xtgnbVo.setSjgbnm(topgnb.getSjgnbm());

            if (topgnb.getSjgnbm() != null){
                if (topgnb.getSjgnbm().equals(gnbm)) {
                    rootMenu.add(xtgnbVo);
                }
            }
            topGnbs.add(xtgnbVo);
        }

        //遍历为根节点设置子节点列表
        for (XtgnbVo xtgnbVo : rootMenu) {
            //获取根节点下所有字节点
            List<XtgnbVo> childCategory = findChildCategory(topGnbs, xtgnbVo.getGnbm());
            //给根节点设置子节点
            xtgnbVo.setChildren(childCategory);
        }

        return rootMenu;

    }


    /**
     * 对按钮包装
     * @param list
     * @return
     */
    public XtgnbBo getUserXtgnbBo(List<String> list) throws UnsupportedEncodingException {

        //判断是否查出结果集 如果没有就直接返回空；
        if (list == null){
            return null;
        }

        XtgnbBo xtgnbBo = new XtgnbBo();
        List<XtgnbBo01> xtgnbBo01s = new ArrayList<>();
        List<XtgnbBo02> xtgnbBo02s = new ArrayList<>();
        List<Xtgnb> byGnbm = xtgnbRepository.findByGnbm(list);


        //遍历所有的功能进行重新分装
        for (Xtgnb xtgnb : byGnbm) {
            XtgnbBo01 xtgnbBo01 = new XtgnbBo01();
            XtgnbBo02 xtgnbBo02 = new XtgnbBo02();
            System.out.println("xtgnb:---"+xtgnb);
            xtgnbBo01.setHandler("on"+xtgnb.getGnbm()+"_click");
            xtgnbBo01.setIconCls(xtgnb.getIconcls());
            xtgnbBo01.setMenutype(xtgnb.getMenuType());
            xtgnbBo01.setText(xtgnb.getGnmc());
            xtgnbBo01.setScope("controller");
            xtgnbBo01.setShowbystatus(xtgnb.getShowbystatus());
            xtgnbBo01.setGnbm(xtgnb.getGnbm());
            xtgnbBo02.setGnbm(xtgnb.getGnbm());
            try {
                xtgnbBo02.setAfterclick(Base64ImageUtil.byteArr2String(xtgnb.getAfterclick()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                xtgnbBo02.setBeforeclick(Base64ImageUtil.byteArr2String(xtgnb.getBeforeclick()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            xtgnbBo01s.add(xtgnbBo01);
            xtgnbBo02s.add(xtgnbBo02);

        }

        xtgnbBo.setViewlist(xtgnbBo01s);
        xtgnbBo.setMethodlist(xtgnbBo02s);

        System.out.println("xtgnbBo"+xtgnbBo);
        return xtgnbBo;
    }


    public List<Long> getAllId(){
        List<Long> allId = xtgnbRepository.getAllId();
        System.out.println("allId"+allId);
        return null;
    }

}
