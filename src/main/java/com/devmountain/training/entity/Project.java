package com.devmountain.training.entity;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "PROJECT")
public class Project  {
//    private Logger logger = LoggerFactory.getLogger(Project.class);

    public Project() {
    }

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name ="name")
    private String name;

    @Column(name ="description")
    private String description;

    @Column(name = "create_date")
    private LocalDate createDate;

    @ManyToMany(mappedBy = "projects", fetch = FetchType.LAZY)
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "student_project",
//            joinColumns = { @JoinColumn(name = "project_id") },
//            inverseJoinColumns = { @JoinColumn(name = "student_id") }
//    )
    /*
     * In Hibernate, the best practice is to use Set instead of List.
     * the reason we use List here is we are facing set.remove(obj) does not work properly.
     * We could not figure out why.
     */
    private List<Student> students;

    public List<Student> getStudents() {
        if(students == null)
            students = new ArrayList<Student>();
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    /*
     *  this is a convenient utility kind of method to add the relationship between
     *  this project and this input student
     */
    public void addStudent(Student student) {
        student.getProjects().add(this);
        this.getStudents().add(student);
    }

    /*
     *  this is a convenient utility kind of method to remove the relationship
     *  between this project and this input student
     */
    public boolean removeStudent(Student student) {
        boolean successfulFlag = student.getProjects().remove(this);
        successfulFlag = getStudents().remove(student);
        return successfulFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return Objects.equals(getId(), project.getId()) && Objects.equals(getName(), project.getName()) && Objects.equals(getDescription(), project.getDescription()) && Objects.equals(getCreateDate(), project.getCreateDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getCreateDate());
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createDate=" + createDate +
                '}';
    }

//    @Override
//    public int compareTo(Project project) {
//        int equalIntValue = 0;
//        if(project != null && project.getId() != null && this.getId() != null)
//            equalIntValue = (int)(this.getId() - project.getId());
//        return equalIntValue;
//    }
}
