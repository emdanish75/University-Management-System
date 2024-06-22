package com.Alzarrar.UniversityManagement.Service;

import com.Alzarrar.UniversityManagement.Entity.AdminEntity;
import com.Alzarrar.UniversityManagement.Entity.StudentEntity;

import java.util.List;

public interface StudentService {
    List<StudentEntity> findAll();
    StudentEntity findByStudentId(Integer studentId);
    void delete(Integer studentId);
    boolean existsByStudentId(Integer studentId);
    List<StudentEntity> findByFullNameContaining(String keyword);
    Boolean existsByUsername(String username);
    StudentEntity save(StudentEntity entity);

    void deleteByName(String name);




}

