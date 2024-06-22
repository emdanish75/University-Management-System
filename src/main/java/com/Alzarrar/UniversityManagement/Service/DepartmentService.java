package com.Alzarrar.UniversityManagement.Service;

import com.Alzarrar.UniversityManagement.Entity.DepartmentEntity;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    List<DepartmentEntity> findAll();

    DepartmentEntity findById(String departmentName);

    Optional<DepartmentEntity> findByDepartmentName(String departmentName);

    Boolean existsByDepartmentCode(String departmentCode);

    Boolean existsByDepartmentName(String departmentName);

    List<DepartmentEntity> findByNameContaining(String departmentName);

    DepartmentEntity save(DepartmentEntity entity);

    void delete(String departmentName);

    // Add any other methods for specific operations related to the DepartmentEntity
}
