package com.devmountain.training.service;

import com.devmountain.training.dao.MajorDao;
import com.devmountain.training.dao.ProjectDao;
import com.devmountain.training.dao.StudentDao;
import com.devmountain.training.entity.Student;
import com.devmountain.training.hibernate.MajorDaoHibernateImpl;
import com.devmountain.training.mapper.StudentMapper;
import com.devmountain.training.model.StudentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
//    @Qualifier("jdbcMajorDao")
    @Qualifier("hibernateMajorDao")
    private MajorDao majorDao;

    @Autowired
    @Qualifier("jdbcProjectDao")
//    @Qualifier("hibernateProjectDao")
    private ProjectDao projectDao;

    @Autowired
    @Qualifier("jdbcStudentDao")
//    @Qualifier("hibernateStudentDao")
    private StudentDao studentDao;


    @Autowired
    private StudentMapper studentMapper;

    @Override
    public StudentDto getStudentById(Long studentId) {
        Student student = studentDao.getStudentById(studentId);
        StudentDto studentDto = studentMapper.studentToStudentDto(student);
        return studentDto;
    }

    @Override
    public StudentDto saveNewStudent(StudentDto studentDto) {
        Student student = studentDao.save(studentMapper.studentDtoToStudent(studentDto));
        return studentMapper.studentToStudentDto(student);
    }

    @Override
    public void updateStudent(Long studentId, StudentDto studentDto) {
        studentDto.setId(studentId);
        studentDao.update(studentMapper.studentDtoToStudent(studentDto));
    }

    @Override
    public void deleteStudentById(Long studentId) {
        studentDao.deleteById(studentId);
    }

    public String dealWithPizza(int orderValue) {
        /*
         *  dsajfndslfns
         */
        //sdlgfdslg
        //dfgd        ksl
        //sdgfds
        String returnString = null;
//                boolean delieverFlag = false;
//                if(orderValue > 50) {
//                    //do something for order > 50
//                    delieverFlag = true;
//                } else {
//                    //do something here
//                    delieverFlag = false;
//                }
         if(isOrderDelieverable(orderValue))
             returnString = "OK, I am happy";

         return returnString;
    }


    private boolean isOrderDelieverable(int orderValue) {
        boolean delieverFlag = false;
        if(orderValue > 50)
            delieverFlag = true;
        return delieverFlag;
    }



}
