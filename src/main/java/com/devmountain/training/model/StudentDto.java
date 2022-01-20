package com.devmountain.training.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDto {
    private Long id;
    private String loginName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private LocalDate enrolledDate;

}
