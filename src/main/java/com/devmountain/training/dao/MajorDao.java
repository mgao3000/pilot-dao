package com.devmountain.training.dao;

import com.devmountain.training.entity.Major
        ;

import java.util.List;

public interface MajorDao {
    Major save(Major major);
    Major update(Major major);
    boolean deleteByName(String majorName);
    boolean deleteById(Long majorId);
    boolean delete(Major major);
    List<Major> getMajors();
    Major getMajorById(Long id);
    Major getMajorByName(String majorName);

    List<Major> getMajorsWithChildren();
    Major getMajorAndStudentsAndProjectsByMajorId(Long majorId);
    Major getMajorAndStudentsAndProjectsByMajorName(String majorName);

}
