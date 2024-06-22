package com.Alzarrar.UniversityManagement.Service;

import com.Alzarrar.UniversityManagement.Entity.TeacherEntity;
import com.Alzarrar.UniversityManagement.repositry.TeacherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService{

    private final TeacherRepo teacherRepo;
    
    @Override
    public Boolean existsByFullNameIgnoreCase(String name) {
        return teacherRepo.existsByFullNameIgnoreCase(name);
    }

    @Override
    public List<TeacherEntity> findByFullName(String name) {
        return teacherRepo.findByFullName(name);
    }

    @Override
    public List<TeacherEntity> findAllByFullNameIgnoreCaseOrDepartmentName(String name, String department) {
        return teacherRepo.findAllByFullNameIgnoreCaseOrDepartmentName(name , department);
    }

    @Override
    public List<TeacherEntity> findByTeacherId(Integer id) {
        return teacherRepo.findByTeacherId(id);
    }

    @Override
    public TeacherEntity getByFullNameIgnoreCase(String name) {
        return teacherRepo.getByFullNameIgnoreCase(name);
    }

    @Override
    public TeacherEntity save(TeacherEntity teacherEntity) {
        return teacherRepo.save(teacherEntity);
    }

    @Override
    public void delete(Integer teacherId) {
        teacherRepo.deleteById(teacherId);
    }

    @Override
    public List<TeacherEntity> findAll() {
        return teacherRepo.findAll();
    }

    @Override
    public Boolean existsByUsername(String username) {
        return teacherRepo.existsByUsernameIgnoreCase(username);
    }

    @Override
    public Boolean existsByUsernameIgnoreCase(String username) {
        return teacherRepo.existsByUsernameIgnoreCase(username);
    }
    @Override
    public void deleteByName(String name) {
        teacherRepo.deleteByName(name);
    }


}
