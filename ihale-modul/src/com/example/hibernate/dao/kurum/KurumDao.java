package com.example.hibernate.dao.kurum;

import com.google.inject.Inject;
import org.hibernate.SessionFactory;

/**
 * Created by Kubra on 2.03.2017.
 */
public class KurumDao  extends BaseDao<Kurum> {

    @Inject
    public KurumDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}

