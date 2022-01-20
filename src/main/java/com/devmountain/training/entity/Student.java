package com.devmountain.training.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "STUDENT")
public class Student  {

    public Student() {
    }

    public Student(String loginName, String password, String firstName,
                   String lastName, String email, String address) {
        this.loginName = loginName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name ="login_name")
    private String loginName;

    @Column(name ="password")
    private String password;

    @Column(name ="first_name")
    private String firstName;

    @Column(name ="last_name")
    private String lastName;

    @Column(name ="email")
    private String email;

    @Column(name ="address")
    private String address;

    @Column(name = "enrolled_date")
    private LocalDate enrolledDate;

    @JsonIgnore
//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
//    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

//    @ManyToMany(mappedBy = "students", cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
//    @ManyToMany(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "student_project",
            joinColumns = { @JoinColumn(name = "student_id") },
            inverseJoinColumns = { @JoinColumn(name = "project_id") }
    )
    /*
     * In Hibernate, the best practice is to use Set instead of List.
     * the reason we use List here is we are facing set.remove(obj) does not work properly.
     * We could not figure out why.
     */
    private List<Project> projects;

    public List<Project> getProjects() {
        if(projects == null)
            projects = new ArrayList<Project>();
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    /*
     *  this is a convenient utility kind of method to add the relationship between
     *  this student and this input project
     */
    public void addProject(Project project) {
        this.getProjects().add(project);
        project.getStudents().add(this);
    }

    /*
     *  this is a convenient utility kind of method to remove the relationship
     *  between this student and this input project
     */
    public void removeProject(Project project) {
        this.getProjects().remove(project);
        project.getStudents().remove(this);
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
//        major.addStudent(this);
    }

    /*
     *  this is a convenient utility kind of method to remove the relationship
     *  between this student and this input project
     */
    public void removeMajor() {
        Major associatedMajor = getMajor();
        if(associatedMajor != null) {
            associatedMajor.removeStudent(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }

    public void setEnrolledDate(LocalDate enrolledDate) {
        this.enrolledDate = enrolledDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
//        return Objects.equals(getId(), student.getId()) && Objects.equals(getLoginName(), student.getLoginName()) && Objects.equals(getPassword(), student.getPassword()) && Objects.equals(getFirstName(), student.getFirstName()) && Objects.equals(getLastName(), student.getLastName()) && Objects.equals(getEmail(), student.getEmail()) && Objects.equals(getAddress(), student.getAddress()) && Objects.equals(getEnrolledDate(), student.getEnrolledDate());
        return Objects.equals(getId(), student.getId()) && Objects.equals(getLoginName(), student.getLoginName()) && Objects.equals(getPassword(), student.getPassword()) && Objects.equals(getFirstName(), student.getFirstName()) && Objects.equals(getLastName(), student.getLastName()) && Objects.equals(getEmail(), student.getEmail()) && Objects.equals(getAddress(), student.getAddress());
//        return Objects.equals(getId(), student.getId());
    }

    @Override
    public int hashCode() {
//        return Objects.hash(getId(), getLoginName(), getPassword(), getFirstName(), getLastName(), getEmail(), getAddress(), getEnrolledDate());
        return Objects.hash(getId(), getLoginName(), getPassword(), getFirstName(), getLastName(), getEmail(), getAddress());
//        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", loginName='" + loginName + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", enrolledDate=" + enrolledDate +
                '}';
    }

//    @Override
//    public int compareTo(Student student) {
//        int equalIntValue = 0;
//        if(student != null && student.getId() != null && this.getId() != null)
//            equalIntValue = (int)(this.getId() - student.getId());
//        return equalIntValue;
//    }
}
