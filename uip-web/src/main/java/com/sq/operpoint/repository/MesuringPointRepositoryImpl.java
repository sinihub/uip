package com.sq.operpoint.repository;


import com.sq.operpoint.domain.Constant;
import com.sq.protocol.opc.domain.MesuringPoint;

import javax.persistence.*;
import java.util.List;

/**
 * Created by ywj on 2016/1/25.
 */
public class MesuringPointRepositoryImpl {

    private EntityManagerFactory emf;
    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    /**
     * 获取所有记录数
     *
     * @param sourceName
     * @return
     */
    public Integer countMesuringPoint(String sourceName) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        StringBuilder nativeSql = new StringBuilder();
        if (sourceName != null && !"".equals(sourceName)) {
            nativeSql.append("select t.* from t_mesuringpoint t where t.sourceCode like '%" + sourceName + "%' or t.pointName like '%" + sourceName + "%'");
        } else {
            nativeSql.append("select t.* from t_mesuringpoint t  ");
        }
        Query query = em.createNativeQuery(nativeSql.toString(), MesuringPoint.class);
        List<MesuringPoint> MesuringPoint = query.getResultList();
        em.getTransaction().commit();
        em.close();
        return MesuringPoint.size();
    }


    /**
     * 分页查询
     *
     * @param pageNo
     * @return
     */
    public List<MesuringPoint> MesuringPointPage(final Integer pageNo, String sourceName) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        StringBuilder nativeSql = new StringBuilder();
        if (sourceName != null && !"".equals(sourceName)) {
            nativeSql.append("select t.* from t_mesuringpoint t where t.sourceCode like '%" + sourceName + "%' or t.pointName like '%" + sourceName + "%'");
        } else {
            nativeSql.append("select t.* from t_mesuringpoint t  ");
        }
        Query query = em.createNativeQuery(nativeSql.toString(), MesuringPoint.class);
        query.setFirstResult((pageNo - Constant.PAGE_LAST_ADD_ONE) * Constant.eight);
        query.setMaxResults(Constant.eight);
        List<MesuringPoint> MesuringPoint = query.getResultList();
        em.getTransaction().commit();
        em.close();
        return MesuringPoint;
    }
}
