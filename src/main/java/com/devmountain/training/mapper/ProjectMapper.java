package com.devmountain.training.mapper;

import com.devmountain.training.entity.Project;
import com.devmountain.training.model.ProjectDto;
import org.mapstruct.Mapper;

@Mapper
public interface ProjectMapper {
    ProjectDto projectToProjectDto(Project project);

    Project projectDtoToProject(ProjectDto projectDto);
}
