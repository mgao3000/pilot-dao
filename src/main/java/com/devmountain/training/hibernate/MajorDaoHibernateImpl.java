package com.devmountain.training.hibernate;

import com.devmountain.training.dao.MajorDao;
import com.devmountain.training.entity.Major;
import com.devmountain.training.exception.EntityCannotBeDeletedDueToNonEmptyChildrenException;
import com.devmountain.training.exception.EntityNotExistException;
import com.devmountain.training.util.HQLStatementUtil;
import com.devmountain.training.util.HibernateUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("hibernateMajorDao")
public class MajorDaoHibernateImpl extends AbstractDaoHibernateImpl implements MajorDao {
    private Logger logger = LoggerFactory.getLogger(MajorDaoHibernateImpl.class);

    @Override
    public Major save(Major major) {
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            transaction = session.beginTransaction();
//            long id = (long) session.save(major);
//            if(id == 0) {
//                logger.error("Fail to insert a major = {}", major);
//                throw new TrainingEntitySaveOrUpdateFailedException("Fail to insert a major = " + major);
//            } else {
//                major.setId(id);
//            }
            session.saveOrUpdate(major);
            transaction.commit();
        } catch (Exception e) {
            logger.error("fail to insert a major, error={}", e.getMessage());
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return major;
    }

    @Override
    public Major update(Major major) {
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(major);
            transaction.commit();
            return major;
        } catch (Exception e) {
            logger.error("fail to update major, error={}", e.getMessage());
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    /**
     * Using HQL to do deletion
     */
    public boolean deleteByName(String majorName) {
        int deleteCount = 0;
        Major retrievedMajor = getMajorAndStudentsAndProjectsByMajorName(majorName);
        if(retrievedMajor == null) {
            throw new EntityNotExistException("Cannot find the Major by the majorName to be deleted. input majorName = " + majorName);
        }
        if(!majorHasStudents(retrievedMajor)) {
            Transaction transaction = null;
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                transaction = session.beginTransaction();
                Query<Major> query = session.createQuery(HQLStatementUtil.HQL_DELETE_MAJOR_BY_NAME);
                query.setParameter("name", majorName);
                deleteCount = query.executeUpdate();
                transaction.commit();
            } catch (HibernateException he) {
                logger.error("fail to delete major by majorName={}, error={}",
                        majorName, he.getMessage());
                if(transaction != null)
                    transaction.rollback();
            } finally {
                session.close();
            }
        } else {
            throw new EntityCannotBeDeletedDueToNonEmptyChildrenException("Cannot delete the Major because there are " +
                    "still some students are associated with the major, major = " + retrievedMajor);
        }

        return (deleteCount > 0) ? true : false;
    }

    private boolean majorHasStudents(Major retrievedMajor) {
        boolean hasStudent = false;
        if(retrievedMajor.getStudents() != null && retrievedMajor.getStudents().size() > 0)
            hasStudent = true;
        return hasStudent;
    }

    @Override
    /**
     * Deleting a persistent instance
     */
    public boolean deleteById(Long majorId) {
        boolean deleteResult = false;
        Major retrievedMajor = getMajorAndStudentsAndProjectsByMajorId(majorId);
        if(retrievedMajor == null) {
            throw new EntityNotExistException("Cannot find the Major by the majorId to be deleted. input majorId = " + majorId);
        }
        if(!majorHasStudents(retrievedMajor)) {
            Transaction transaction = null;
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                transaction = session.beginTransaction();
                deleteResult = deleteById(session, Major.class, majorId);
                transaction.commit();
            } catch (HibernateException he) {
                logger.error("fail to delete major by majorId={}, error={}",
                        majorId, he.getMessage());
                if(transaction != null)
                    transaction.rollback();
            } finally {
                session.close();
            }
        } else {
            throw new EntityCannotBeDeletedDueToNonEmptyChildrenException("Cannot delete the Major because there are " +
                    "still some students are associated with the major, major = " + retrievedMajor);
        }
        return deleteResult;
    }

    @Override
    public boolean delete(Major major) {
        return deleteById(major.getId());
//        return deleteByName(major.getName());
    }

    @Override
    public List<Major> getMajors() {
        List<Major> majorList = null;
        try (Session session = HibernateUtil.getSession()) {
            Query<Major> query = session.createQuery(HQLStatementUtil.HQL_SELECT_ALL_MAJORS);

            majorList = query.list();
        } catch (HibernateException he) {
            logger.error("fail to retrieve all majors, error={}", he.getMessage());
        }
        if(majorList == null)
            majorList = new ArrayList<Major>();
        return majorList;
    }

    @Override
    public List<Major> getMajorsWithChildren() {
        List<Major> majorList = null;
        try (Session session = HibernateUtil.getSession()) {
            Query<Major> query = session.createQuery(HQLStatementUtil.HQL_SELECT_ALL_MAJORS_WITH_CHILDREN);

            majorList = query.list();
        } catch (HibernateException he) {
            logger.error("fail to retrieve all majors, error={}", he.getMessage());
        }
        if(majorList == null)
            majorList = new ArrayList<Major>();
        return majorList;
    }

    @Override
    public Major getMajorById(Long id) {
        return getMajorByIdAndHQL(id, HQLStatementUtil.HQL_SELECT_MAJOR_BY_ID);
    }

    @Override
    public Major getMajorAndStudentsAndProjectsByMajorId(Long majorId) {
        return getMajorByIdAndHQL(majorId, HQLStatementUtil.HQL_SELECT_MAJOR_WITH_CHILDREN_BY_MAJOR_ID);
    }

    private Major getMajorByIdAndHQL(Long majorId, String hqlStatement) {
        Major retrievedMajor = null;
        if(majorId != null) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Major> query = session.createQuery(hqlStatement);
                query.setParameter("id", majorId);

                retrievedMajor = query.uniqueResult();
            } catch (HibernateException he) {
                logger.error("fail to retrieve major by majorId={}, hqlStatement={}, error={}", majorId, hqlStatement, he.getMessage());
            }
        }
        return retrievedMajor;
    }

    @Override
    public Major getMajorByName(String majorName) {
        return getMajorByNameAndHQL(majorName, HQLStatementUtil.HQL_SELECT_MAJOR_BY_NAME);
    }

    @Override
    public Major getMajorAndStudentsAndProjectsByMajorName(String majorName) {
        return getMajorByNameAndHQL(majorName, HQLStatementUtil.HQL_SELECT_MAJOR_WITH_CHILDREN_BY_MAJOR_NAME);
    }

    private Major getMajorByNameAndHQL(String majorName, String hqlStatement) {
        Major retrievedMajor = null;
        if(!StringUtils.isEmpty(majorName)) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Major> query = session.createQuery(hqlStatement);
                query.setParameter("name", majorName);

                retrievedMajor = query.uniqueResult();
            } catch (HibernateException he) {
                logger.error("fail to retrieve major by majorName={}, hqlStatement={}, error={}", majorName, hqlStatement, he.getMessage());
            }
        }
        return retrievedMajor;
    }

}
