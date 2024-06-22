package com.Alzarrar.UniversityManagement.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Data
@Entity
@Getter
@Setter
@Table(schema = "university", name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "User_ID")
    private Integer userID;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "Username", nullable = false)
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "Role", nullable = false)
    private String role;
    // No-arg constructor for JPA
    public UserEntity() {
    }
    // Constructor for projection
    public UserEntity(String email, String username) {
        this.email = email;
        this.username = username;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserEntity that = (UserEntity) obj;
        return Objects.equals(email, that.email) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username);
    }
}
