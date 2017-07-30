package com.example.hibernate.dao.ihale;

import com.google.inject.Inject;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by Kubra on 21.02.2017.
 */
public class IhaleDao extends BaseDao<Ihale> {

@Inject
    public IhaleDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Ihale> ikneGoreIhaleGetir(String ikn){

        Criteria criteria=currentSession().createCriteria(Ihale.class,"i").add(Restrictions.eq("i.ikn",ikn));


        return criteria.list() ;
    }
    public List<Ihale> aktiflikDurumaGoreIhaleGetir(Boolean ihaleAktiflikDurum){

        Criteria criteria=currentSession().createCriteria(Ihale.class,"i").add(Restrictions.eq("i.ihaleAktiflikDurum",ihaleAktiflikDurum));


        return criteria.list() ;
    }






}


