package com.devmountain.training.hibernate;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public abstract class AbstractDaoHibernateImpl {
    private Logger logger = LoggerFactory.getLogger(AbstractDaoHibernateImpl.class);

    /**
     * This method is utility method to delete a persistent instance by ID
     * @param session
     * @param type
     * @param id
     * @return
     */
    protected boolean deleteById(Session session, Class<?> type, Serializable id) {
        Object persistentInstance = session.load(type, id);
        if (persistentInstance != null) {
            session.delete(persistentInstance);
            logger.info("===========$$$$$$ Within AbstractDaoHibernateImpl.deleteById, input id={}, persistentInstance is notnot NULL", id);
            return true;
        }
        logger.info("===========$$$$$$ Within AbstractDaoHibernateImpl.deleteById, it seems persistentInstance is NULL");
        return false;
    }
}
