package com.devmountain.training.hibernate;

import com.devmountain.training.dao.ProjectDao;
import com.devmountain.training.entity.Major;
import com.devmountain.training.entity.Project;
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

@Repository("hibernateProjectDao")
public class ProjectDaoHibernateImpl extends AbstractDaoHibernateImpl implements ProjectDao {
    private Logger logger = LoggerFactory.getLogger(ProjectDaoHibernateImpl.class);

    @Override
    public Project save(Project project) {
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            transaction = session.beginTransaction();
//            long id = (long) session.save(project);
//            if(id == 0) {
//                logger.error("Fail to insert a project = {}", project);
//                throw new TrainingEntitySaveOrUpdateFailedException("Fail to insert a project = " + project);
//            } else {
//                project.setId(id);
//            }
            session.saveOrUpdate(project);
            transaction.commit();
        } catch (Exception e) {
            logger.error("fail to insert a project, error={}", e.getMessage());
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return project;
    }

    @Override
    public Project update(Project project) {
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(project);
            transaction.commit();
            return project;
        } catch (Exception e) {
            logger.error("fail to update project, error={}", e.getMessage());
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
    public boolean deleteByName(String projectName) {
        int deleteCount = 0;
        Project retrievedProject = getProjectWithAssociatedStudentsByName(projectName);
        if(retrievedProject == null) {
            throw new EntityNotExistException("Cannot find the Project by the projectName to be deleted. input projectName = " + projectName);
        }
        if(nonStudentsAssociatedWithProject(retrievedProject)) {
            Transaction transaction = null;
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                transaction = session.beginTransaction();
                Query<Major> query = session.createQuery(HQLStatementUtil.HQL_DELETE_PROJECT_BY_NAME);
                query.setParameter("name", projectName);
                deleteCount = query.executeUpdate();
                transaction.commit();
            } catch (HibernateException he) {
                logger.error("fail to delete project by projectName={}, error={}",
                        projectName, he.getMessage());
                if(transaction != null)
                    transaction.rollback();
            } finally {
                session.close();
            }
        } else {
            throw new EntityCannotBeDeletedDueToNonEmptyChildrenException("Cannot delete the Project because there are " +
                    "still some students are associated with the Project, project = " + retrievedProject);
        }

        return (deleteCount > 0) ? true : false;
    }

    private boolean nonStudentsAssociatedWithProject(Project retrievedProject) {
        boolean noStudentsAtAll = false;
        if(retrievedProject.getStudents().size() == 0)
            noStudentsAtAll = true;
        return noStudentsAtAll;
    }

    @Override
    /**
     * Deleting a persistent instance
     */
    public boolean deleteById(Long projectId) {
        boolean deleteResult = false;
        Project retrievedProject = getProjectWithAssociatedStudentsById(projectId);
//        logger.info("================#####$$$$$$$$, retrievedProject.getStudents().size()={}", retrievedProject.getStudents().size());
        if(retrievedProject == null) {
            throw new EntityNotExistException("Cannot find the Project by the projectId to be deleted. input projectId = " + projectId);
        }
        if(nonStudentsAssociatedWithProject(retrievedProject)) {
            Transaction transaction = null;
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            try {
                transaction = session.beginTransaction();
                deleteResult = deleteById(session, Project.class, projectId);
                transaction.commit();
            } catch (Exception e) {
                logger.error("fail to delete project by projectId={}, error={}", projectId, e.getMessage());
                if(transaction != null)
                    transaction.rollback();
            } finally {
                session.close();
            }
        } else {
            throw new EntityCannotBeDeletedDueToNonEmptyChildrenException("Cannot delete the Project because there are " +
                    "still some students are associated with the Project, project = " + retrievedProject);
        }

        return deleteResult;
    }

    @Override
    public boolean delete(Project project) {
        return deleteById(project.getId());
    }

    @Override
    public List<Project> getProjects() {
        List<Project> projectList = new ArrayList<>();
        try (Session session = HibernateUtil.getSession()) {
            Query<Project> query = session.createQuery(HQLStatementUtil.HQL_SELECT_ALL_PROJECTS);

            projectList = query.list();
        } catch (HibernateException he) {
            logger.error("fail to retrieve all projects, error={}", he.getMessage());
        }
        return projectList;
    }

    @Override
    public List<Project> getProjectsWithAssociatedStudents() {
        List<Project> projectList = null;
        try (Session session = HibernateUtil.getSession()) {
            Query<Project> query = session.createQuery(HQLStatementUtil.HQL_SELECT_ALL_PROJECTS_WITH_ASSOCIATED_STUDENTS);

            projectList = query.list();
        } catch (HibernateException he) {
            logger.error("fail to retrieve all projects, error={}", he.getMessage());
        }
        if(projectList == null)
            projectList = new ArrayList<Project>();
        return projectList;
    }

    @Override
    public Project getProjectById(Long projectId) {
        return getProjectByIdAndHQL(projectId, HQLStatementUtil.HQL_SELECT_PROJECT_BY_ID);
    }

    @Override
    public Project getProjectWithAssociatedStudentsById(Long projectId) {
        return getProjectByIdAndHQL(projectId, HQLStatementUtil.HQL_SELECT_PROJECT_WITH_ASSOCIATED_STUDENTS_BY_PROJECT_ID);
    }

    private Project getProjectByIdAndHQL(Long projectId, String hqlStatement) {
        Project retrievedProject = null;
        if(projectId != null) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Project> query = session.createQuery(hqlStatement);
                query.setParameter("id", projectId);

                retrievedProject = query.uniqueResult();
            } catch (HibernateException he) {
                logger.error("fail to retrieve project by projectId={}, hqlStatement={}, error={}", projectId, hqlStatement, he.getMessage());
            }
        }
        return retrievedProject;
    }

    @Override
    public Project getProjectByName(String projectName) {
        return getProjectByNameAndHQL(projectName, HQLStatementUtil.HQL_SELECT_PROJECT_BY_NAME);
    }

    @Override
    public Project getProjectWithAssociatedStudentsByName(String projectName) {
        return getProjectByNameAndHQL(projectName, HQLStatementUtil.HQL_SELECT_PROJECT_WITH_ASSOCIATED_STUDENTS_BY_PROJECT_NAME);
    }

    private Project getProjectByNameAndHQL(String projectName, String hqlStatement) {
        Project retrievedProject = null;
        if(!StringUtils.isEmpty(projectName)) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Project> query = session.createQuery(hqlStatement);
                query.setParameter("name", projectName);

                retrievedProject = query.uniqueResult();
            } catch (HibernateException he) {
                logger.error("fail to retrieve Project by projectName={}, hqlStatement={}, error={}", projectName, hqlStatement, he.getMessage());
            }
        }
        return retrievedProject;
    }
}
