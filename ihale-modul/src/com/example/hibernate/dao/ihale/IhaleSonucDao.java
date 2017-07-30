package com.example.hibernate.dao.ihale;

import com.google.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Kubra on 14.03.2017.
 */
public class IhaleSonucDao extends BaseDao<IhaleSonuc> {

@Inject
    public IhaleSonucDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<IhaleSonuc> ihaleyeGoreSonucGetir(String ihaleOid){

        Criteria criteria=currentSession().createCriteria(IhaleSonuc.class,"IhaleSonuc").createAlias("IhaleSonuc.ihale","i").add(Restrictions.eq("i.oid",ihaleOid));


    return criteria.list() ;
    } }
