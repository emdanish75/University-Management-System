package com.Alzarrar.UniversityManagement.repositry;

import com.Alzarrar.UniversityManagement.Entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer> {

    Boolean existsByEmailIgnoreCase(String email);

    Optional<UserEntity> getByUsernameIgnoreCase(String username);

    List<UserEntity> findAllByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);

    List<UserEntity> getAllByUsernameIgnoreCaseContaining(String username);

    @Query("SELECT new com.Alzarrar.UniversityManagement.Entity.UserEntity(u.email, u.username) FROM UserEntity u")
    List<UserEntity> findEmailAndUsername();

    List<UserEntity> findByRole(String role);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserEntity u WHERE u.username = :username AND u.email = :email")
    void deleteByUsernameAndEmail(String username, String email);

    // New method to check if a username exists in the users table
    Boolean existsByUsernameIgnoreCase(String username);
}
