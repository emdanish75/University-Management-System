package com.Alzarrar.UniversityManagement.repositry;

import com.Alzarrar.UniversityManagement.Entity.CoursesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoursesRepo extends JpaRepository<CoursesEntity, Integer> {

    Optional<CoursesEntity> findByCourseCodeIgnoreCase(String courseCode);

    List<CoursesEntity> findAllByCourseNameIgnoreCase(String courseName);

    List<CoursesEntity> findAllByDepartment_DepartmentNameIgnoreCase(String departmentName);

    Boolean existsByCourseCodeIgnoreCase(String courseCode);

    Boolean existsByCourseNameIgnoreCase(String courseName);

    // Add any additional methods based on your requirements
}
