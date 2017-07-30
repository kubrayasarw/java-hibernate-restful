package com.example.hibernate.dao.ihale;

import com.google.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Kubra on 29.03.2017.
 */

public class IhaleYorumDao extends BaseDao<IhaleYorum> {
@Inject
    public IhaleYorumDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<IhaleYorum> ihaleyeGoreYorumGetir(String ihaleOid){
        Criteria criteria=currentSession().createCriteria(IhaleYorum.class,"IhaleYorum").createAlias("IhaleYorum.ihale","i").add(Restrictions.eq("i.oid",ihaleOid));


        return criteria.list() ;


    }
}
