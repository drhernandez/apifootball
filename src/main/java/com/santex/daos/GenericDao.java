package com.santex.daos;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T extends Serializable> {

    /**
     *
     * @param entity: entity to save
     * @return Identifier of saved entity
     */
    Serializable save(T entity);

    /**
     *
     * @param entity:entity to save or update
     */
    void saveOrUpdate(T entity);

    /**
     *
     * @param entity:entity to update
     */
    void update(T entity);

    /**
     *
     * @param entity: entity to delete
     */
    void delete(T entity);

    /**
     * Delete a record by id
     * @param id
     */
    void deleteById(Serializable id);

    /**
     * Find all records
     * @return
     */
    List<T> findAll();

    /**
     * Find by primary key
     * @param id
     * @return unique entity
     */
    T findById(Serializable id);

    /**
     * Clear session
     */
    void clear();

    /**
     * Flush session
     */
    void flush();
}
