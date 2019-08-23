package com.santex.daos.imp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public abstract class AbstractGenericDao<T extends Serializable> {

    private final Class<T> entityClass;
    private SessionFactory sessionFactory;

    public AbstractGenericDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Serializable save(T entity) {
        return getSession().save(entity);
    }

    public void saveOrUpdate(T entity) {
        getSession().saveOrUpdate(entity);
    }

    public void update(T entity) {
        getSession().merge(entity);
    }

    public void delete(T entity) {
        getSession().delete(entity);
    }

    public void deleteById(Serializable id) {
        T entity = findById(id);
        delete(entity);
    }

    public List<T> findAll() {
        return getSession().createQuery("from " + entityClass.getName()).list();
    }

    public T findById(Serializable id) {
        return getSession().get(this.entityClass, id);
    }

    public void clear() {
        getSession().clear();
    }

    public void flush() {
        getSession().flush();
    }

    protected Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }
}
