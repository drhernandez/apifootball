package com.santex.daos.imp;

import com.google.inject.Inject;
import com.santex.daos.CompetitionsDao;
import com.santex.models.entities.Competition;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class CompetitionsDaoImp
        extends AbstractGenericDao<Competition>
        implements CompetitionsDao {

    @Inject
    public CompetitionsDaoImp(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Competition findByCode(String competitionCode) {

        String hql = "from Competition WHERE code = :code";
        Query query = getSession().createQuery(hql);
        query.setParameter("code", competitionCode);
        return  (Competition) query.getSingleResult();
    }
}
