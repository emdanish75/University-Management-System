package com.Alzarrar.UniversityManagement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@Entity
@Getter
@Setter
@Table(schema = "university", name = "students")
public class StudentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Integer studentId;
    @Column(name = "full_name")
    @NotBlank(message = "Full name must be entered")
    private String fullName;
    @Column(name = "username",unique = true)
    @NotBlank(message = "Username must be entered")
    private String username;
    @Column(name = "email")
    @NotBlank(message = "Email must be entered")
    private String email;
    @Column(name = "password")
    @NotBlank(message = "password must be entered")
    private String password;
    @Column(name = "gender")
    @NotBlank(message = "Gender must be entered")
    private String gender;
    @Column(name = "phoneNo")
    @NotBlank(message = "Phone number must be entered")
    private String phoneNo;
    @Column(name = "age")
    @NotNull(message = "Age must be entered")
    private Integer Age;
    @Column(name = "semester")
    @NotNull(message = "Semester must be entered")
    private Integer semester;
    @Column(name = "department")
    private String department;
}
