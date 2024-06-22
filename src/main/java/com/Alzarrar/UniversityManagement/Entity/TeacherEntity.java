package com.Alzarrar.UniversityManagement.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Table(schema = "university", name = "teachers")
public class TeacherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Integer teacherId;

    @Column(name ="full_name", nullable = false)
    @NotBlank(message = "Full name must be entered")
    private String fullName;

    @Column(name ="Email", nullable = false)
    @NotBlank(message = "Email must be entered")
    private String email;

    @Column(name ="Username",unique = true, nullable = false)
    @NotBlank(message = "Username must be entered")
    private String username;

    @Column(name ="Experience", nullable = false)
    @NotNull(message = "Experience years must be entered")
    private Integer experienceYears;

    @Column(name ="City", nullable = false)
    @NotBlank(message = "City years must be entered")
    private String city;

    @Column(name = "Department_Name", nullable = false)
    @NotBlank(message = "Department years must be entered")
    private String departmentName;

    @Column(name = "Course_name", nullable = false)
    @NotBlank(message = "Course years must be entered")
    private String course;

    @Column(name = "Salary", nullable = false)
    @NotNull(message = "Salary must be entered")
    private double salary;

    @Column(name = "Password")
    private String password;
    public TeacherEntity() {
    }
}

