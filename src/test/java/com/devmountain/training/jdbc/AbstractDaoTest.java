package com.devmountain.training.jdbc;

import com.devmountain.training.dao.*;
import com.devmountain.training.entity.Major;
import com.devmountain.training.entity.Project;
import com.devmountain.training.entity.Student;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class AbstractDaoTest {
    private Logger logger = LoggerFactory.getLogger(AbstractDaoTest.class);

    protected static MajorDao majorDao;

    protected static StudentDao studentDao;

    protected static ProjectDao projectDao;

    protected String tempMajorName;

    protected String tempProjectName;

    protected String tempLoginName = null;

    protected String tempEmail = null;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    protected void assertMajors(Major randomMajor, Major retrievedMajor) {
        assertEquals(randomMajor.getId(), retrievedMajor.getId());
        assertEquals(randomMajor.getName(), retrievedMajor.getName());
        assertEquals(randomMajor.getDescription(), retrievedMajor.getDescription());
//        assertEquals(randomMajor.getStudents().size(), retrievedMajor.getStudents().size());
    }

    protected void assertProjects(Project randomProject, Project retrievedProject) {
        assertEquals(randomProject.getId(), retrievedProject.getId());
        assertEquals(randomProject.getName(), retrievedProject.getName());
        assertEquals(randomProject.getDescription(), retrievedProject.getDescription());
        assertTrue(randomProject.getCreateDate().isEqual(retrievedProject.getCreateDate()));
//        assertEquals(randomProject.getStudents().size(), retrievedProject.getStudents().size());
    }

    protected void assertStudents(Student randomStudent, Student retrievedStudent) {
        assertEquals("both student IDs should be equal", randomStudent.getId(), retrievedStudent.getId());
        assertEquals(randomStudent.getMajor().getId(), retrievedStudent.getMajor().getId());
        assertEquals(randomStudent.getLoginName(), retrievedStudent.getLoginName());
        assertEquals(randomStudent.getPassword(), retrievedStudent.getPassword());
        assertEquals(randomStudent.getFirstName(), retrievedStudent.getFirstName());
        assertEquals(randomStudent.getLastName(), retrievedStudent.getLastName());
        assertEquals(randomStudent.getEmail(), retrievedStudent.getEmail());
        assertEquals(randomStudent.getAddress(), retrievedStudent.getAddress());
        assertTrue(randomStudent.getEnrolledDate().isEqual(retrievedStudent.getEnrolledDate()));
    }

    protected Project createProjectByName(String projectName) {
        Project project = new Project();
        project.setName(projectName);
        project.setDescription(projectName + "--description");
        project.setCreateDate(LocalDate.now());
        return project;
    }

    protected Major createMajorByName(String name) {
        Major major = new Major();
        major.setName(name);
        major.setDescription(name + "--description");
        return major;
    }

    protected Student createStudentByLoginNameAndEmailWithRandomMajor(String loginName, String email) {
        Student student = new Student();
        student.setLoginName(loginName);
        student.setPassword("password123456");
        student.setFirstName("Frist name test");
        student.setLastName("Last name test");
        student.setEmail(email);
        student.setAddress("123 Dodge Road, Reston, VA 20220");
        student.setEnrolledDate(LocalDate.now());

        /*
         * Now using MajorDao to select a valid Major from DB
         */
        Major randomMajor = getRandomMajor();
        student.setMajor(randomMajor);

        return student;
    }

    protected Student createStudentByLoginNameAndEmailWithoutMajorAssigned(String loginName, String email) {
        Student student = new Student();
        student.setLoginName(loginName);
        student.setPassword("password123456");
        student.setFirstName("Frist name test");
        student.setLastName("Last name test");
        student.setEmail(email);
        student.setAddress("123 Dodge Road, Reston, VA 20220");
        student.setEnrolledDate(LocalDate.now());

        return student;
    }

    protected Project getRandomProject() {
        List<Project> projectList = projectDao.getProjects();
        Project randomProject = null;
        if(projectList != null && projectList.size() > 0) {
            int randomIndex = getRandomInt(0, projectList.size());
            randomProject = projectList.get(randomIndex);
        }
        return randomProject;
    }

    protected Major getRandomMajor() {
        List<Major> majorList = majorDao.getMajors();
        Major randomMajor = null;
        if(majorList != null && majorList.size() > 0) {
            int randomIndex = getRandomInt(0, majorList.size());
            randomMajor = majorList.get(randomIndex);
        }
        return randomMajor;
    }

    protected Student getRandomStudent() {
        List<Student> studentList = studentDao.getStudents();
        Student randomStudent = null;
        if(studentList != null && studentList.size() > 0) {
            int randomIndex = getRandomInt(0, studentList.size());
            randomStudent = studentList.get(randomIndex);
        }
        return randomStudent;
    }

    protected void displayStudentsWithoutAssociatedProjects(List<Student> studentList) {
        logger.info("The total number of Students is: {}", studentList.size());
        int index = 1;
        for(Student student : studentList) {
            logger.info("No.{} Student:", index);
            displayStudentWithoutAssociatedProjects(student);
            index++;
        }
    }

    protected void displayStudentWithoutAssociatedProjects(Student student) {
        logger.info(" Students is: {}", student);
    }

    protected void displayStudents(List<Student> studentList) {
        logger.info("The total number of Students is: {}", studentList.size());
        int index = 1;
        for(Student student : studentList) {
            logger.info("No.{} Student:", index);
            displayStudent(student);
            index++;
        }
    }

    protected void displayProjects(List<Project> projectList) {
        logger.info("The total number of Projects is: {}", projectList.size());
        int index = 1;
        for(Project eachProject : projectList) {
            logger.info("No.{} project:", index);
            displayProject(eachProject);
            index++;
        }
    }

    protected void displayProjectsWithoutAssociatedStudents(List<Project> projectList) {
        logger.info("The total number of Projects is: {}", projectList.size());
        int index = 1;
        for(Project eachProject : projectList) {
            logger.info("No.{} project:", index);
            displayProjectWithoutAssociatedStudents(eachProject);
            index++;
        }
    }

    protected void displayProject(Project project) {
        logger.info("Project detail={}", project);
        displayStudentsWithoutAssociatedProjects(project.getStudents());
    }

    protected void displayProjectWithoutAssociatedStudents(Project project) {
        logger.info("Project detail={}", project);
    }

    protected void displayStudentSetWithAssociatedProjects(Set<Student> studentSet) {
        logger.info("\t The total associated students={}", studentSet.size());
        int index = 1;
        for (Student student : studentSet) {
            logger.info("No.{} student = {}", index, student);
            displayProjectList(student.getProjects());
            logger.info("-----------------------------");
            index++;
        }
    }

//    protected void displayStudentsWithoutAssociatedProjects(List<Student> studentSet) {
//        logger.info("\t The total associated students={}", studentSet.size());
//        int index = 1;
//        for (Student student : studentSet) {
//            logger.info("No.{} student = {}", index, student);
//            logger.info("========= Student's major info as below: ");
//            displayMajorWithoutChildren(student.getMajor());
//            index++;
//        }
//    }

    protected void displayMajorsWithoutChildren(List<Major> majorList) {
        logger.info("The total number of Majors is: {}", majorList.size());
        int index = 1;
        for(Major major : majorList) {
            logger.info("No.{} Major:", index);
            logger.info("Major detail={}", major);
            logger.info("===============================================");
            index++;
        }
    }

    protected void displayMajorsWithChildren(List<Major> majorList) {
        logger.info("The total number of Majors is: {}", majorList.size());
        int index = 1;
        for(Major major : majorList) {
            logger.info("No.{} Major:", index);
            displayMajorWithAssociatedStudents(major);
            logger.info("===============================================");
            index++;
        }
    }

    protected void displayMajorWithAssociatedStudents(Major major) {
        logger.info("Major detail={}", major);
        displayStudentSetWithAssociatedProjects(major.getStudents());
    }

    protected void displayMajorWithoutChildren(Major major) {
        logger.info("Major detail={}", major);
    }


//    protected void displayStudentList(List<StudentModel> studentList) {
//        logger.info("The total number of Students is: {}", studentList.size());
//        int index = 1;
//        for(StudentModel student : studentList) {
//            logger.info("No.{} Student:", index);
//            displayStudent(student);
//            index++;
//        }
//    }

    protected void displayStudent(Student student) {
        logger.info("Student detail={}", student);
        displayProjectList(student.getProjects());
    }

    protected void displayProjectList(List<Project> projectSet) {
        logger.info("\t The total associated projects={}", projectSet.size());
        int index = 1;
        for (Project project : projectSet) {
            logger.info("No.{} project = {}", index, project);
            index++;
        }
        logger.info("");
    }


    /**
     * This helper method return a random int value in a range between
     * min (inclusive) and max (exclusive)
     * @param min
     * @param max
     * @return
     */
    public int getRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

}
