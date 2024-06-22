package com.Alzarrar.UniversityManagement.repositry;

import com.Alzarrar.UniversityManagement.Entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepo extends JpaRepository<DepartmentEntity, String> {

    Boolean existsByDepartmentCodeIgnoreCase(String departmentCode);

    Boolean existsByDepartmentNameIgnoreCase(String departmentName);

    List<DepartmentEntity> getAllByDepartmentNameIgnoreCaseContaining(String departmentName);

    // Add any other methods for specific queries related to the DepartmentEntity
}
