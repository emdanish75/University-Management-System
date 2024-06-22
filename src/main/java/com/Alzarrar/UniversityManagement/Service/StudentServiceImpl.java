package com.Alzarrar.UniversityManagement.Service;
import com.Alzarrar.UniversityManagement.Entity.AdminEntity;
import com.Alzarrar.UniversityManagement.Entity.StudentEntity;
import com.Alzarrar.UniversityManagement.repositry.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepo studentRepo;
    @Override
    public List<StudentEntity> findAll() {
        return studentRepo.findAll();
    }
    @Override
    public StudentEntity findByStudentId(Integer studentId) {
        return null;
    }
    @Override
    public boolean existsByStudentId(Integer studentId) {
        return studentRepo.existsByStudentId(studentId);
    }
    @Override
    public void delete(Integer studentId) {
        studentRepo.deleteById(studentId);
    }
    @Override
    public List<StudentEntity> findByFullNameContaining(String keyword) {
        return studentRepo.findByFullNameContaining(keyword);
    }
    @Override
    public Boolean existsByUsername(String username) {
        return studentRepo.existsByUsernameIgnoreCase(username);
    }
    @Override
    public StudentEntity save(StudentEntity entity) {
        if (entity == null) {
            throw new RuntimeException("Entity is null");
        }
        return studentRepo.saveAndFlush(entity);
    }

    @Override
    public void deleteByName(String name){
        studentRepo.deleteByName(name);
    }
}


