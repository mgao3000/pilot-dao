package com.devmountain.training.mapper;

import com.devmountain.training.entity.Student;
import com.devmountain.training.model.StudentDto;
import org.mapstruct.Mapper;

@Mapper
public interface StudentMapper {
    StudentDto studentToStudentDto(Student student);

    Student studentDtoToStudent(StudentDto studentDto);
}
