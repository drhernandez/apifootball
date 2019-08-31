package com.santex.daos.imp;

import com.google.inject.Inject;
import com.santex.daos.TeamsDao;
import com.santex.models.entities.Team;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class TeamsDaoImp
        extends AbstractGenericDao<Team>
        implements TeamsDao {

    @Inject
    public TeamsDaoImp(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<Team> findByIds(List<Long> ids) {

        String hql = "from Team WHERE id IN (:ids)";
        Query query = getSession().createQuery(hql);
        query.setParameter("ids", ids);
        List<Team> teams = query.list();
        return teams != null ? teams : new ArrayList<>();
    }
}
