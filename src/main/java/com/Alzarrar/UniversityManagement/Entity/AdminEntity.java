package com.Alzarrar.UniversityManagement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Entity
@Table(schema = "university", name = "admins")
@Data
public class AdminEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Integer adminId;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name must be entered")
    private String name;

    @Column(name = "username", nullable = false, unique = true)
    @NotBlank(message = "Username must be entered")
    private String username;

    @Column(name = "email", nullable = false)
    @NotBlank(message = "Email must be entered")
    private String email;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "Password")
    private String password;

    @Column(name = "salary", nullable = false)
    @NotNull(message = "Salary must be entered")
    private Double salary;
}
