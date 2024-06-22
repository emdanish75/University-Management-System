package com.Alzarrar.UniversityManagement.Service;

import com.Alzarrar.UniversityManagement.Entity.TeacherEntity;

import java.util.List;

public interface TeacherService {
    List<TeacherEntity> findAll();

    Boolean existsByUsername(String username);


    Boolean existsByFullNameIgnoreCase(String name);
    List<TeacherEntity> findByFullName(String name);
    List<TeacherEntity> findAllByFullNameIgnoreCaseOrDepartmentName(String name , String department );
    List<TeacherEntity> findByTeacherId(Integer id);
    TeacherEntity getByFullNameIgnoreCase(String name);



    TeacherEntity save(TeacherEntity teacherEntity);
    void delete(Integer teacherId);
    Boolean existsByUsernameIgnoreCase (String username);
    void deleteByName(String name);

}
