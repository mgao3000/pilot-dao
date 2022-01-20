package com.devmountain.training.service;

import com.devmountain.training.model.MajorDto;

public interface MajorService {
    MajorDto getMajorById(Long majorId);
    MajorDto saveNewMajor(MajorDto majorDto);
    void updateMajor(Long projectId, MajorDto majorDto);
    void deleteMajorById(Long majorId);
}
