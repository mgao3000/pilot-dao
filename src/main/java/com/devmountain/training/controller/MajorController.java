package com.devmountain.training.controller;

import com.devmountain.training.service.MajorService;
import com.devmountain.training.service.ProjectService;
import com.devmountain.training.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/major")
@RestController
public class MajorController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private MajorService majorService;

    @Autowired
    private ProjectService projectService;


}
