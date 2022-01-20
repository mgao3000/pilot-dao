package com.devmountain.training.dao;

import com.devmountain.training.entity.Project;

import java.util.List;

public interface ProjectDao {
    Project save(Project project);
    Project update(Project project);
    boolean deleteByName(String projectName);
    boolean deleteById(Long projectId);
    boolean delete(Project project);
    List<Project> getProjects();
    Project getProjectById(Long id);
    Project getProjectByName(String projectName);
    List<Project> getProjectsWithAssociatedStudents();
    Project getProjectWithAssociatedStudentsById(Long projectId);
    Project getProjectWithAssociatedStudentsByName(String projectName);
}
