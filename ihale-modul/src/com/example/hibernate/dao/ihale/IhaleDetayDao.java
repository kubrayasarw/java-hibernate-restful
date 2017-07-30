package com.example.hibernate.dao.ihale;

import com.google.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Kubra on 23.02.2017.
 */

public class IhaleDetayDao extends BaseDao<IhaleDetay> {

    @Inject
    public IhaleDetayDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<IhaleDetay> ihaleyeGoreDetayGetir(String ihaleOid) {

        Criteria criteria=currentSession().createCriteria(IhaleDetay.class,"iDetay")
                .createAlias("iDetay.ihale","i")
                .add(Restrictions.eq("i.oid",ihaleOid));

        return criteria.list();

    }


}
