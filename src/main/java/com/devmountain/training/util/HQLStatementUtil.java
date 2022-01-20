package com.devmountain.training.util;

public class HQLStatementUtil {

    /*
     * Major related HQL Statements
     */
    public static final String HQL_SELECT_ALL_MAJORS = "From Major";
    public static final String HQL_SELECT_MAJOR_BY_ID = "FROM Major as m where m.id = :id";
    public static final String HQL_SELECT_MAJOR_BY_NAME = "FROM Major as m where m.name = :name";
    public static final String HQL_SELECT_ALL_MAJORS_WITH_CHILDREN = "SELECT distinct m FROM Major as m " +
            "left join fetch m.students as student left join fetch student.projects";
    public static final String HQL_SELECT_MAJOR_WITH_CHILDREN_BY_MAJOR_NAME = "SELECT distinct m FROM Major as m " +
            "left join fetch m.students as student left join fetch student.projects where m.name = :name";
    public static final String HQL_SELECT_MAJOR_WITH_CHILDREN_BY_MAJOR_ID = "SELECT distinct m FROM Major as m " +
            "left join fetch m.students as student left join fetch student.projects where m.id = :id";

//    public static final String HQL_DELETE_MAJOR_BY_ID = "DELETE Major as m where m.id = :id";
    public static final String HQL_DELETE_MAJOR_BY_NAME = "DELETE Major as m where m.name = :name";

    /*
     * Project related HQL Statements
     */
    public static final String HQL_SELECT_ALL_PROJECTS = "From Project";
    public static final String HQL_SELECT_PROJECT_BY_ID = "FROM Project as project where project.id = :id";
    public static final String HQL_SELECT_PROJECT_BY_NAME = "FROM Project as project where project.name = :name";
    public static final String HQL_SELECT_ALL_PROJECTS_WITH_ASSOCIATED_STUDENTS = "SELECT distinct proj FROM " +
            "Project as proj left join fetch proj.students as student left join fetch student.major";
    public static final String HQL_SELECT_PROJECT_WITH_ASSOCIATED_STUDENTS_BY_PROJECT_ID = "SELECT distinct proj " +
            "FROM Project as proj left join fetch proj.students as student left join fetch student.major where proj.id=:id";
    public static final String HQL_SELECT_PROJECT_WITH_ASSOCIATED_STUDENTS_BY_PROJECT_NAME = "SELECT distinct proj " +
            "FROM Project as proj left join fetch proj.students as student left join fetch student.major where proj.name=:name";

//    public static final String HQL_DELETE_PROJECT_BY_ID = "DELETE Project as project where project.id = :id";
    public static final String HQL_DELETE_PROJECT_BY_NAME = "DELETE Project as project where project.name = :name";

    /*
     * Project related HQL Statements
     */
    public static final String HQL_SELECT_STUDENT_ID_LOGIN_NAME = "SELECT id From Student as student where student.loginName = :loginName";
    public static final String HQL_SELECT_ALL_STUDENTS = "From Student";
    public static final String HQL_SELECT_STUDENT_BY_ID = "FROM Student as student where student.id = :id";
    public static final String HQL_SELECT_STUDENT_BY_LOGIN_NAME = "FROM Student as student where student.loginName = :loginName";
    public static final String HQL_SELECT_ASSOCIATED_PROJECTS_BY_STUDENT_LOGIN_NAME = "SELECT student.projects From Student as student where student.loginName = :loginName";
    public static final String HQL_SELECT_ASSOCIATED_PROJECTS_BY_STUDENT_ID = "SELECT student.projects From Student as student where student.id = :id";
    public static final String HQL_SELECT_ALL_STUDENTS_WITH_ASSOCIATED_PROJECTS = "SELECT distinct stu FROM " +
            "Student as stu left join fetch stu.projects as proj left join fetch stu.major";
    public static final String HQL_SELECT_STUDENT_WITH_ASSOCIATED_PROJECTS_BY_STUDENT_ID = "SELECT distinct stu FROM " +
            "Student as stu left join fetch stu.projects as proj left join fetch stu.major where stu.id=:id";
    public static final String HQL_SELECT_STUDENT_WITH_ASSOCIATED_PROJECTS_BY_STUDENT_LOGIN_NAME = "SELECT distinct stu FROM " +
            "Student as stu left join fetch stu.projects as proj left join fetch stu.major where stu.loginName=:loginName";

    public static final String HQL_DELETE_STUDENT_BY_LOGIN_NAME = "DELETE Student as student where student.loginName = :loginName";

}
