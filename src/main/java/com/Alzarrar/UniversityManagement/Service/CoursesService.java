package com.Alzarrar.UniversityManagement.Service;

import com.Alzarrar.UniversityManagement.Entity.CoursesEntity;

import java.util.List;
import java.util.Optional;

public interface CoursesService {

    List<CoursesEntity> findAll();

    Optional<CoursesEntity> findByCourseCode(String courseCode);

    List<CoursesEntity> findAllByCourseName(String courseName);

    List<CoursesEntity> findAllByDepartmentName(String departmentName);

    boolean existsByCourseCode(String courseCode);

    boolean existsByCourseName(String courseName);
    CoursesEntity save(CoursesEntity course);

    void delete(Integer courseId);

    // Add any additional methods based on your requirements
}
