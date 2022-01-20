package com.devmountain.training.service;

import com.devmountain.training.model.StudentDto;

public interface StudentService {
    StudentDto getStudentById(Long studentId);
    StudentDto saveNewStudent(StudentDto studentDto);
    void updateStudent(Long studentId, StudentDto studentDto);
    void deleteStudentById(Long studentId);
}
