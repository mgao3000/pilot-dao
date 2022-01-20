package com.devmountain.training.controller;

import com.devmountain.training.model.StudentDto;
import com.devmountain.training.service.MajorService;
import com.devmountain.training.service.ProjectService;
import com.devmountain.training.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RequestMapping("/api/student")
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private MajorService majorService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDto> getStudentByid(@PathVariable("studentId") Long studentId) {
        ResponseEntity<StudentDto> responseEntity = new ResponseEntity<>(studentService.getStudentById(studentId), HttpStatus.OK);
        return responseEntity;
    }

    @PostMapping
    public ResponseEntity saveStudent(@Valid @RequestBody StudentDto studentDto) {
        StudentDto savedCustomerDto = studentService.saveNewStudent(studentDto);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/student/" + savedCustomerDto.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity updateStudent(@PathVariable("studentId") Long studentId, @Valid @RequestBody StudentDto studentDto) {
        studentService.updateStudent(studentId, studentDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable("studentId")Long studentId) {
        studentService.getStudentById(studentId);

    }
}
