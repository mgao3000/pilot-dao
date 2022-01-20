package com.devmountain.training.hibernate;

import com.devmountain.training.dao.StudentDao;
import com.devmountain.training.entity.Major;
import com.devmountain.training.entity.Project;
import com.devmountain.training.entity.Student;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository("hibernateStudentDao")
public class StudentDaoHibernateImpl extends AbstractDaoHibernateImpl implements StudentDao {
    private Logger logger = LoggerFactory.getLogger(StudentDaoHibernateImpl.class);

    @Override
    public Student save(Student student) {
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();

        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(student);
            transaction.commit();
        } catch (Exception e) {
            logger.error("fail to insert a student, error={}", e.getMessage());
            if(transaction != null)
                transaction.rollback();
        } finally {
            session.close();
        }
        return student;
    }

    @Override
    public Student update(Student student) {
        Transaction transaction = null;
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            transaction = session.beginTransaction();
            session.saveOrUpdate(student);
            transaction.commit();
            return student;
        } catch (Exception e) {
            logger.error("fail to update student, error={}", e.getMessage());
            if(transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return null;
    }

    @Override
    public boolean deleteByLoginName(String loginName) {
        int deleteCount = 0;
        Student retrievedStudent = getStudentWithAssociatedProjectsByLoginName(loginName);
        if(retrievedStudent == null) {
            throw new EntityNotExistException("Cannot find the Student by the loginName to be deleted. input loginName = " + loginName);
        }
        if(!studentHasAssociatedProjects(retrievedStudent)) {
            Transaction transaction = null;
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                transaction = session.beginTransaction();
                Query<Student> query = session.createQuery(HQLStatementUtil.HQL_DELETE_STUDENT_BY_LOGIN_NAME);
                query.setParameter("loginName", loginName);
                deleteCount = query.executeUpdate();
                transaction.commit();
            } catch (HibernateException he) {
                logger.error("fail to delete student by loginName={}, error={}",
                        loginName, he.getMessage());
                if (transaction != null)
                    transaction.rollback();
            } finally {
                session.close();
            }
        } else {
            throw new EntityCannotBeDeletedDueToNonEmptyChildrenException("Cannot delete the Student because there are " +
                    "still some Projects are associated with the Student, student.loginName = " + loginName);
        }

        return (deleteCount > 0) ? true : false;
    }

    private boolean studentHasAssociatedProjects(Student retrievedStudent) {
        boolean hasProjects = false;
        if(retrievedStudent.getProjects() != null && retrievedStudent.getProjects().size() > 0)
            hasProjects = true;
        return hasProjects;
    }

    private boolean isThereSomeAssociatedProjectsExist(String loginName) {
        boolean doesProjectsExist = false;
        List<Project> projectList = getAssociatedProjectsByStudentLoginName(loginName);
        if(projectList != null && projectList.size() > 0)
            doesProjectsExist = true;
        return doesProjectsExist;
    }

    @Override
    public boolean deleteById(Long studentId) {
        boolean deleteResult = false;
        Student retrievedStudent = getStudentWithAssociatedProjectsByStudentId(studentId);
        if(retrievedStudent == null) {
            throw new EntityNotExistException("Cannot find the Student by the studentId to be deleted. input studentId = " + studentId);
        }
        if(!studentHasAssociatedProjects(retrievedStudent)) {
            Transaction transaction = null;
            Session session = HibernateUtil.getSessionFactory().openSession();
            try {
                transaction = session.beginTransaction();
                deleteResult = deleteById(session, Student.class, studentId) ;
                transaction.commit();
            } catch (Exception e) {
                logger.error("fail to delete student by studentId={}, error={}", studentId, e.getMessage());
                if (transaction != null)
                    transaction.rollback();
            } finally {
                session.close();
            }
        } else {
            throw new EntityCannotBeDeletedDueToNonEmptyChildrenException("Cannot delete the Student because there are " +
                    "still some Projects are associated with the Student, StudentId = " + studentId);
        }
        return deleteResult;
    }

    @Override
    public boolean delete(Student student) {
        return deleteById(student.getId());
    }

    @Override
    public List<Student> getStudents() {
        List<Student> studentList = new ArrayList<>();
        try (Session session = HibernateUtil.getSession()) {
            Query<Student> query = session.createQuery(HQLStatementUtil.HQL_SELECT_ALL_STUDENTS);

            studentList = query.list();
        } catch (HibernateException he) {
            logger.error("fail to retrieve all students, error={}", he.getMessage());
        }
        return studentList;
    }

    @Override
    public List<Student> getStudentsWithAssociatedProjects() {
        List<Student> studentList = new ArrayList<>();
        try (Session session = HibernateUtil.getSession()) {
            Query<Student> query = session.createQuery(HQLStatementUtil.HQL_SELECT_ALL_STUDENTS_WITH_ASSOCIATED_PROJECTS);

            studentList = query.list();
        } catch (HibernateException he) {
            logger.error("fail to retrieve all students with associated projects, error={}", he.getMessage());
        }
        return studentList;
    }

    @Override
    public Student getStudentById(Long studentId) {
        return getStudentByIdAndHQL(studentId, HQLStatementUtil.HQL_SELECT_STUDENT_BY_ID);
    }

    @Override
    public Student getStudentWithAssociatedProjectsByStudentId(Long studentId) {
        return getStudentByIdAndHQL(studentId, HQLStatementUtil.HQL_SELECT_STUDENT_WITH_ASSOCIATED_PROJECTS_BY_STUDENT_ID);
    }

    private Student getStudentByIdAndHQL(Long id, String hqlStatement) {
        Student retrievedStudent = null;
        if(id != null) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Student> query = session.createQuery(hqlStatement);
                query.setParameter("id", id);

                retrievedStudent = query.uniqueResult();
            } catch (HibernateException he) {
                logger.error("fail to retrieve student by studentId={}, error={}", id, he.getMessage());
            }
        }
        return retrievedStudent;
    }

    @Override
    public Student getStudentByLoginName(String loginName) {
        return getStudentByLoginNameAndHQL(loginName, HQLStatementUtil.HQL_SELECT_STUDENT_BY_LOGIN_NAME);
    }

    @Override
    public Student getStudentWithAssociatedProjectsByLoginName(String loginName) {
        return getStudentByLoginNameAndHQL(loginName, HQLStatementUtil.HQL_SELECT_STUDENT_WITH_ASSOCIATED_PROJECTS_BY_STUDENT_LOGIN_NAME);
    }

    private Student getStudentByLoginNameAndHQL(String loginName, String hqlStatement) {
        Student retrievedStudent = null;
        if(!StringUtils.isEmpty(loginName)) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Student> query = session.createQuery(hqlStatement);
                query.setParameter("loginName", loginName);

                retrievedStudent = query.uniqueResult();
            } catch (HibernateException he) {
                logger.error("fail to retrieve student and the associated projects by loginName={}, error={}", loginName, he.getMessage());
            }
        }
        return retrievedStudent;
    }

    @Override
    public List<Project> getAssociatedProjectsByStudentId(Long studentId) {
        List<Project> projectList = new ArrayList<>();
        if(studentId != null) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Project> query = session.createQuery(HQLStatementUtil.HQL_SELECT_ASSOCIATED_PROJECTS_BY_STUDENT_ID);
                query.setParameter("id", studentId);

                projectList = query.list();
            } catch (HibernateException he) {
                logger.error("fail to retrieve the associated Projects by studentId={}, error={}", studentId, he.getMessage());
            }
        }
        return projectList;
    }

    @Override
    public List<Project> getAssociatedProjectsByStudentLoginName(String loginName) {
        List<Project> projectList = new ArrayList<>();
        if(!StringUtils.isEmpty(loginName)) {
            try(Session session = HibernateUtil.getSession()) {
                Query<Project> query = session.createQuery(HQLStatementUtil.HQL_SELECT_ASSOCIATED_PROJECTS_BY_STUDENT_LOGIN_NAME);
                query.setParameter("loginName", loginName);

                projectList = query.list();
            } catch (HibernateException he) {
                logger.error("fail to retrieve the associated Projects by student.loginName={}, error={}", loginName, he.getMessage());
            }
        }
        return projectList;
    }
}
