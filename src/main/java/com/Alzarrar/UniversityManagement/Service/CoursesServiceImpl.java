package com.Alzarrar.UniversityManagement.Service;

import com.Alzarrar.UniversityManagement.Entity.CoursesEntity;
import com.Alzarrar.UniversityManagement.repositry.CoursesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoursesServiceImpl implements CoursesService {

    private final CoursesRepo coursesRepo;

    @Autowired
    public CoursesServiceImpl(CoursesRepo coursesRepo) {
        this.coursesRepo = coursesRepo;
    }

    @Override
    public List<CoursesEntity> findAll() {
        return coursesRepo.findAll();
    }

    @Override
    public Optional<CoursesEntity> findByCourseCode(String courseCode) {
        return coursesRepo.findByCourseCodeIgnoreCase(courseCode);
    }

    @Override
    public List<CoursesEntity> findAllByCourseName(String courseName) {
        return coursesRepo.findAllByCourseNameIgnoreCase(courseName);
    }

    @Override
    public List<CoursesEntity> findAllByDepartmentName(String departmentName) {
        return coursesRepo.findAllByDepartment_DepartmentNameIgnoreCase(departmentName);
    }

    @Override
    public boolean existsByCourseCode(String courseCode) {
        return coursesRepo.existsByCourseCodeIgnoreCase(courseCode);
    }

    @Override
    public boolean existsByCourseName(String courseName) {
        return coursesRepo.existsByCourseNameIgnoreCase(courseName);
    }

    @Override
    public CoursesEntity save(CoursesEntity course) {
        return coursesRepo.save(course);
    }

    @Override
    public void delete(Integer courseId) {
        coursesRepo.deleteById(courseId);
    }

    // Add any additional methods based on your requirements
}
