package com.Alzarrar.UniversityManagement.repositry;

import com.Alzarrar.UniversityManagement.Entity.AdminEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepo extends JpaRepository<AdminEntity, Integer> {

    Boolean existsByEmailIgnoreCase(String email);

    AdminEntity getByEmailIgnoreCase(String email);

    List<AdminEntity> findAllByRoleIgnoreCase(String role);

    List<AdminEntity> findAllByNameIgnoreCaseOrCityIgnoreCase(String name, String city);

    @Transactional
    @Modifying
    @Query("DELETE FROM AdminEntity a WHERE a.name = :name")
    void deleteByName(@Param("name") String name);
    Boolean existsByUsernameIgnoreCase(String username);



}
