package com.Alzarrar.UniversityManagement.Service;

import com.Alzarrar.UniversityManagement.Entity.UserEntity;
import java.util.List;

public interface UserService {

    List<UserEntity> findAll();

    List<UserEntity> findByUsername(String username);

    Boolean existsByUsername(String username);

    UserEntity save(UserEntity entity);

    void delete(Integer userID);

    List<UserEntity> findByRole(String name);

    List<UserEntity> findEmailAndUsername();

    UserEntity saveWithPassword(UserEntity entity);

    void deleteByUsernameAndEmail(String username, String email);

    // New method to check if a username exists in the users table
    Boolean existsByUsernameInUsersTable(String username);
}
