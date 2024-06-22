package com.Alzarrar.UniversityManagement.repositry;

import com.Alzarrar.UniversityManagement.Entity.TeacherEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TeacherRepo extends JpaRepository<TeacherEntity , Integer> {

    boolean existsByFullNameIgnoreCase(String name);
    List<TeacherEntity> findByFullName(String name);
    List<TeacherEntity> findAllByFullNameIgnoreCaseOrDepartmentName(String name , String department );
    List<TeacherEntity> findByTeacherId(Integer id);
    TeacherEntity getByFullNameIgnoreCase(String name);

    TeacherEntity deleteByTeacherId(Integer id);


    Boolean existsByUsernameIgnoreCase(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM TeacherEntity a WHERE a.fullName = :fullName")
    void deleteByName(@Param("fullName") String fullName);
}
