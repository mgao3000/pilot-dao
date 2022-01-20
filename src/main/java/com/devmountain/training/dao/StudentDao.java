package com.devmountain.training.dao;

import com.devmountain.training.entity.Project;
import com.devmountain.training.entity.Student;

import java.util.List;

public interface StudentDao {
    Student save(Student student);
    Student update(Student student);
    boolean deleteByLoginName(String loginName);
    boolean deleteById(Long studentId);
    boolean delete(Student student);
    List<Student> getStudents();
    Student getStudentById(Long id);
    Student getStudentByLoginName(String loginName);
    List<Project> getAssociatedProjectsByStudentId(Long studentId);
    List<Project> getAssociatedProjectsByStudentLoginName(String loginName);

    List<Student> getStudentsWithAssociatedProjects();
    Student getStudentWithAssociatedProjectsByStudentId(Long studentId);
    Student getStudentWithAssociatedProjectsByLoginName(String loginName);
}
