package com.devmountain.training.service;

import com.devmountain.training.dao.MajorDao;
import com.devmountain.training.dao.ProjectDao;
import com.devmountain.training.dao.StudentDao;
import com.devmountain.training.model.ProjectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    @Qualifier("jdbcMajorDao")
//    @Qualifier("hibernateMajorDao")
    private MajorDao majorDao;

    @Autowired
    @Qualifier("jdbcProjectDao")
//    @Qualifier("hibernateProjectDao")
    private ProjectDao projectDao;

    @Autowired
    @Qualifier("jdbcStudentDao")
//    @Qualifier("hibernateStudentDao")
    private StudentDao studentDao;



    @Override
    public ProjectDto getProjectById(Long projectId) {
        return null;
    }

    @Override
    public ProjectDto saveNewProject(ProjectDto projectDto) {
        return null;
    }

    @Override
    public void updateProject(Long projectId, ProjectDto projectDto) {

    }

    @Override
    public void deleteProjectById(Long projectId) {

    }
}
