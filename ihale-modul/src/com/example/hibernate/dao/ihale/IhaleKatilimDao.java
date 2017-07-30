package com.example.hibernate.dao.ihale;

import com.google.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Kubra on 7.03.2017.
 */
public class IhaleKatilimDao extends BaseDao<IhaleKatilim> {

    @Inject
    public IhaleKatilimDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }


    public List<IhaleKatilim> ihaleyeGoreKatilimGetir(String ihaleOid) {


        Criteria criteria=currentSession().createCriteria(IhaleKatilim.class,"iKatilim")
                .createAlias("iKatilim.ihale","i")
                .add(Restrictions.eq("i.oid",ihaleOid));

        return criteria.list();

    }






}
