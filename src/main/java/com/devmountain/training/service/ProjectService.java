package com.devmountain.training.service;

import com.devmountain.training.model.ProjectDto;

public interface ProjectService {
    ProjectDto getProjectById(Long projectId);
    ProjectDto saveNewProject(ProjectDto projectDto);
    void updateProject(Long projectId, ProjectDto projectDto);
    void deleteProjectById(Long projectId);
}
