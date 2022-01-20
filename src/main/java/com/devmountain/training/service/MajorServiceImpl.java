package com.devmountain.training.service;

import com.devmountain.training.dao.MajorDao;
import com.devmountain.training.dao.ProjectDao;
import com.devmountain.training.dao.StudentDao;
import com.devmountain.training.model.MajorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MajorServiceImpl implements MajorService {

    @Autowired
    @Qualifier("jdbcMajorDao")
//    @Qualifier("hibernateMajorDao")
    private MajorDao majorDao;

    @Autowired
//    @Qualifier("jdbcProjectDao")
    @Qualifier("hibernateProjectDao")
    private ProjectDao projectDao;

    @Autowired
    @Qualifier("jdbcStudentDao")
//    @Qualifier("hibernateStudentDao")
    private StudentDao studentDao;


    @Override
    public MajorDto getMajorById(Long majorId) {
        return null;
    }

    @Override
    public MajorDto saveNewMajor(MajorDto majorDto) {
        return null;
    }

    @Override
    public void updateMajor(Long projectId, MajorDto majorDto) {

    }

    @Override
    public void deleteMajorById(Long majorId) {

    }
}
