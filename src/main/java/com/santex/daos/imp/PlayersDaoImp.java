package com.santex.daos.imp;

import com.google.inject.Inject;
import com.santex.daos.PlayersDao;
import com.santex.exceptions.NotFoundException;
import com.santex.models.entities.Player;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

public class PlayersDaoImp
        extends AbstractGenericDao<Player>
        implements PlayersDao {

    @Inject
    public PlayersDaoImp(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public long countPlayersByCompetition(String competitionCode) throws NotFoundException {

        String hql = "SELECT count(p) FROM Competition c " +
                "INNER JOIN c.teams t " +
                "INNER JOIN t.players p " +
                "WHERE c.code = :code";
        Query query = getSession().createQuery(hql);
        query.setParameter("code", competitionCode);

        return (long) query.list().get(0);
    }
}
