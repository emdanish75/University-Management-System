package com.Alzarrar.UniversityManagement.repositry;

import com.Alzarrar.UniversityManagement.Entity.StudentEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<StudentEntity, Integer> {

    Boolean existsByStudentId(Integer studentId);

    Boolean existsByFullNameIgnoreCase(String fullName);

    List<StudentEntity> findByFullNameContaining(String keyword);

    List<StudentEntity> findByUsernameIgnoreCase(String userName);
    Boolean existsByUsernameIgnoreCase(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM AdminEntity a WHERE a.name = :name")
    void deleteByName(@Param("name") String name);

}

