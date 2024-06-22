package com.Alzarrar.UniversityManagement.Service;

import com.Alzarrar.UniversityManagement.Entity.UserEntity;
import com.Alzarrar.UniversityManagement.repositry.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    @Override
    public List<UserEntity> findAll() {
        return userRepo.findAll();
    }

    @Override
    public List<UserEntity> findByUsername(String username) {
        return userRepo.getAllByUsernameIgnoreCaseContaining(username);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepo.existsByEmailIgnoreCase(username);
    }

    @Override
    public Boolean existsByUsernameInUsersTable(String username) {
        return userRepo.existsByUsernameIgnoreCase(username);
    }

    @Override
    public UserEntity save(UserEntity entity) {
        if (entity == null) {
            throw new RuntimeException("Entity is null");
        }
        return userRepo.saveAndFlush(entity);
    }

    @Override
    public void delete(Integer userID) {
        userRepo.deleteById(userID);
    }

    @Override
    public List<UserEntity> findByRole(String role) {
        return userRepo.findByRole(role);
    }

    @Override
    public List<UserEntity> findEmailAndUsername() {
        // Assuming you have a method in UserRepo for fetching email and username from the users table
        return userRepo.findEmailAndUsername();
    }

    @Override
    public UserEntity saveWithPassword(UserEntity entity) {
        if (entity == null) {
            throw new RuntimeException("Entity is null");
        }
        return userRepo.saveAndFlush(entity);
    }

    @Override
    public void deleteByUsernameAndEmail(String username, String email) {
        userRepo.deleteByUsernameAndEmail(username, email);
    }

    // Add any additional methods if needed
}
