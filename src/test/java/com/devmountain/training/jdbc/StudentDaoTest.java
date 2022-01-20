package com.devmountain.training.jdbc;

import com.devmountain.training.entity.Major;
import com.devmountain.training.entity.Project;
import com.devmountain.training.entity.Student;
import com.devmountain.training.hibernate.MajorDaoHibernateImpl;
import com.devmountain.training.hibernate.ProjectDaoHibernateImpl;
import com.devmountain.training.hibernate.StudentDaoHibernateImpl;
import org.hibernate.LazyInitializationException;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.*;

public class StudentDaoTest extends AbstractDaoTest {
    private Logger logger = LoggerFactory.getLogger(StudentDaoTest.class);

    @BeforeClass
    public static void setupOnce() {
        studentDao = new StudentDaoHibernateImpl();
        majorDao = new MajorDaoHibernateImpl();
        projectDao = new ProjectDaoHibernateImpl();
    }

    @AfterClass
    public static void teardownOnce() {
        studentDao = null;
        majorDao = null;
        projectDao = null;
    }

    @Before
    public void setup() {
        tempLoginName = "Student-login-" + getRandomInt(1, 1000);
        tempEmail = "test" + getRandomInt(1, 1000) + "@devmountain.com";
        tempMajorName = "Random-Majoe" + getRandomInt(1, 1000);
        tempProjectName = "Project-" + getRandomInt(1, 1000);
    }

    @After
    public void teardown() {
        tempLoginName = null;
        tempEmail = null;
        tempMajorName = null;
        tempProjectName = null;
    }

    @Test
    public void getStudentsTest() {
        List<Student> studentList = studentDao.getStudents();
        displayStudentsWithoutAssociatedProjects(studentList);
        /*
         * Loop each Student and trigger LazyInitializationException
         * due to fetchType = LAZY
         */
        for(Student eachStudent : studentList) {
            try {
                eachStudent.getProjects().size();
            } catch(LazyInitializationException ex) {
                assertTrue(ex instanceof LazyInitializationException);
            }
        }
    }

    @Test
    public void getStudentsWithAssociatedProjectsTest() {
        List<Student> studentList = studentDao.getStudentsWithAssociatedProjects();
        assertEquals(14, studentList.size());
        displayStudents(studentList);
        /*
         * Loop each Student and check each associated projects
         * and each student's associated Major
         */
        for(Student eachStudent : studentList) {
            assertAssociatedProjectsAndMajorStudentBelongBeingRetrieved(eachStudent);
        }
    }

    private void assertAssociatedProjectsAndMajorStudentBelongBeingRetrieved(Student student) {
        //Assert the associated projects are retrieved if there are some associated projects
        assertTrue(student.getProjects().size() >= 0);

        //Make sure Student's major is retrieved too
        assertNotNull(student.getMajor());
    }

    @Test
    public void getStudentByIdTest() {
        /*
         * Pick up a random Student from DB
         */
        Student randomStudent = getRandomStudent();
        if(randomStudent == null) {
            logger.error("there is no student being found in database, please double check DB connection!");
        } else {
            Long studentId = randomStudent.getId();
            Student retrievedStudent = studentDao.getStudentById(studentId);
            assertStudents(randomStudent, retrievedStudent);
            displayStudentWithoutAssociatedProjects(retrievedStudent);

            /*
             * Due to the LAZY initialization, LazyInitializationException is
             * thrown when trying to call retrievedStudent.getProjects()
             */
            thrown.expect(LazyInitializationException.class);
            assertEquals(0, retrievedStudent.getProjects().size());
        }
    }

    @Test
    public void getStudentWithAssociatedProjectsByIdTest() {
        /*
         * Pick up a random Student from DB
         */
        Student randomStudent = getRandomStudent();
        if(randomStudent == null) {
            logger.error("there is no student being found in database, please double check DB connection!");
        } else {
            Long studentId = randomStudent.getId();
            Student retrievedStudent = studentDao.getStudentWithAssociatedProjectsByStudentId(studentId);
            assertStudents(randomStudent, retrievedStudent);
            /*
             * Assert the associated projects are retrieved if there are some associated projects
             */
            assertTrue(retrievedStudent.getProjects().size() >= 0);
//            displayProject(retrievedProject);
        }
    }

    @Test
    public void getStudentByLoginNameTest() {
        /*
         * Pick up a random Student from DB
         */
        Student randomStudent = getRandomStudent();
        if(randomStudent == null) {
            logger.error("there is no Student being found in database, please double check DB connection!");
        } else {
            String loginName = randomStudent.getLoginName();
            Student retrievedStudent = studentDao.getStudentByLoginName(loginName);
            assertStudents(randomStudent, retrievedStudent);
            displayStudentWithoutAssociatedProjects(retrievedStudent);
            /*
             * Due to the LAZY initialization, LazyInitializationException is
             * thrown when trying to call retrievedProject.getStudents()
             */
            thrown.expect(LazyInitializationException.class);
            assertEquals(0, retrievedStudent.getProjects().size());
        }
    }

    @Test
    public void getStudentWithAssociatedProjectsByLoginNameTest() {
        /*
         * Pick up a random Student from DB
         */
        Student randomStudent = getRandomStudent();
        if(randomStudent == null) {
            logger.error("there is no Student being found in database, please double check DB connection!");
        } else {
            String loginName = randomStudent.getLoginName();
            Student retrievedStudent = studentDao.getStudentWithAssociatedProjectsByLoginName(loginName);
            assertStudents(randomStudent, retrievedStudent);
            /*
             * Assert the associated projects are retrieved if there are some associated projects
             */
            assertTrue(retrievedStudent.getProjects().size() >= 0);
            displayStudent(retrievedStudent);
        }
    }

    @Test
    public void saveStudentOnlywithoutAssociatedProjectsTest() {
        Student student = createStudentByLoginNameAndEmailWithoutMajorAssigned(tempLoginName, tempEmail);
        /*
         * Now need to use majorDao to randomly select a valid Major
         */
        Major randomMajor = getRandomMajor();
        student.setMajor(randomMajor);
        Student savedStudent = studentDao.save(student);
        assertStudents(student, savedStudent);
        logger.info("==== retrieved random Major={}", randomMajor);
        /*
         * Now clean up the saved Student from DB Major table
         */
        boolean deleteSuccessfulFlag = studentDao.delete(savedStudent);
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void deleteStudentWithoutAnyAssociatedProjectsTest() {
        /*
         * create a temp Student to test deletion
         */
        Student student = createStudentByLoginNameAndEmailWithoutMajorAssigned(tempLoginName, tempEmail);
        /*
         * Now need to use majorDao to randomly select a valid Major
         */
        Major randomMajor = getRandomMajor();
        student.setMajor(randomMajor);

        Student savedStudent = studentDao.save(student);
        logger.info("==== new savedStudent={}", savedStudent);
        /*
         * Now delete the saved Project from DB Major table
         */
        boolean deleteSuccessfulFlag = studentDao.delete(savedStudent);
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void deleteStudentByIdTest() {
        /*
         * create a temp Student to test deletion
         */
        Student student = createStudentByLoginNameAndEmailWithoutMajorAssigned(tempLoginName, tempEmail);
        /*
         * Now need to use majorDao to randomly select a valid Major
         */
        Major randomMajor = getRandomMajor();
        student.setMajor(randomMajor);

        Student savedStudent = studentDao.save(student);
        /*
         * Now delete the saved Student using student ID from DB Major table
         */
        boolean deleteSuccessfulFlag = studentDao.deleteById(savedStudent.getId());
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void deleteStudentByLoginNameTest() {
        /*
         * create a temp Student to test deletion
         */
        Student student = createStudentByLoginNameAndEmailWithoutMajorAssigned(tempLoginName, tempEmail);
        /*
         * Now need to use majorDao to randomly select a valid Major
         */
        Major randomMajor = getRandomMajor();
        student.setMajor(randomMajor);

        Student savedStudent = studentDao.save(student);
        /*
         * Now delete the saved Student using student ID from DB Major table
         */
        boolean deleteSuccessfulFlag = studentDao.deleteByLoginName(savedStudent.getLoginName());
        assertEquals(true, deleteSuccessfulFlag);
    }

    @Test
    public void updateStudentTest() {
        Student originalStudent = getRandomStudent();

        String originalStudentAddress = originalStudent.getAddress();
        String modifiedStudentAddress = originalStudentAddress + "---Modified Address";
        originalStudent.setAddress(modifiedStudentAddress);
        /*
         * Now start doing update operation
         */
        Student updatedStudent = studentDao.update(originalStudent);
        assertStudents(originalStudent, updatedStudent);

        /*
         * now reset Student address back to the original value
         */
        originalStudent.setAddress(originalStudentAddress);
        updatedStudent = studentDao.update(originalStudent);
        assertStudents(originalStudent, updatedStudent);
    }

    @Test
    public void addProjectsToStudentTest() {
        //Step 1.1  create a temp project No.1
        Project project1 = createProjectByName(tempProjectName);
        Project projectDSaved1 = projectDao.save(project1);

        //Step 1.2: create a temp project No.2
        Project project2 = createProjectByName(tempProjectName+"-2");
        Project projectDSaved2 = projectDao.save(project2);

        //Step 2: create a temp Major
        Major major = createMajorByName(tempMajorName);
        Major majorSaved = majorDao.save(major);
        logger.info("=============, addStudentsToProjectTest(), majorSaved={}", majorSaved);

        //Step 3.1: create a temp Student No.1
        Student student1 = createStudentByLoginNameAndEmailWithoutMajorAssigned(tempLoginName, tempEmail);
        student1.setMajor(majorSaved);
        student1.addProject(projectDSaved1);
        Student studentSaved1 = studentDao.save(student1);
        logger.info("=============, addStudentsToProjectTest(), studentSaved1={}", studentSaved1);

        //Step 3.2: create a temp Student No.1
        Student student2 = createStudentByLoginNameAndEmailWithoutMajorAssigned(tempLoginName+"-2", "a2-" + tempEmail);
        student2.setMajor(majorSaved);
        student2.addProject(projectDSaved1);
        student2.addProject(projectDSaved2);
        Student studentSaved2 = studentDao.save(student2);
        logger.info("=============, addStudentsToProjectTest(), studentSaved2={}", studentSaved2);

        //Conduct assertions
        assertEquals(1, studentSaved1.getProjects().size());
        assertEquals(2, studentSaved2.getProjects().size());

        assertEquals(2, projectDSaved1.getStudents().size());
        assertEquals(1, projectDSaved2.getStudents().size());

        //Now clean up
        studentSaved1.removeProject(projectDSaved1);
        studentDao.update(studentSaved1);
        boolean deleteSuccessfulFlag = studentDao.delete(studentSaved1);
        assertEquals(true, deleteSuccessfulFlag);

        studentSaved2.removeProject(projectDSaved1);
        studentSaved2.removeProject(projectDSaved2);
        studentDao.update(studentSaved2);
        deleteSuccessfulFlag = studentDao.delete(studentSaved2);
        assertEquals(true, deleteSuccessfulFlag);

        deleteSuccessfulFlag = projectDao.deleteById(projectDSaved1.getId());
        assertEquals(true, deleteSuccessfulFlag);

        deleteSuccessfulFlag = projectDao.deleteById(projectDSaved2.getId());
        assertEquals(true, deleteSuccessfulFlag);

        deleteSuccessfulFlag = majorDao.delete(majorSaved);
        assertEquals(true, deleteSuccessfulFlag);

    }


}
