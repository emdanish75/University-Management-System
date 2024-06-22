package com.Alzarrar.UniversityManagement.Service;

import com.Alzarrar.UniversityManagement.Entity.DepartmentEntity;
import com.Alzarrar.UniversityManagement.repositry.DepartmentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepo departmentRepo;

    @Override
    public List<DepartmentEntity> findAll() {
        return departmentRepo.findAll();
    }

    @Override
    public DepartmentEntity findById(String departmentName) {
        return departmentRepo.findById(departmentName).orElse(null);
    }

    @Override
    public Optional<DepartmentEntity> findByDepartmentName(String departmentName) {
        return departmentRepo.findById(departmentName);
    }

    @Override
    public Boolean existsByDepartmentCode(String departmentCode) {
        return departmentRepo.existsByDepartmentCodeIgnoreCase(departmentCode);
    }

    @Override
    public Boolean existsByDepartmentName(String departmentName) {
        return departmentRepo.existsByDepartmentNameIgnoreCase(departmentName);
    }

    @Override
    public List<DepartmentEntity> findByNameContaining(String departmentName) {
        return departmentRepo.getAllByDepartmentNameIgnoreCaseContaining(departmentName);
    }

    @Override
    public DepartmentEntity save(DepartmentEntity entity) {
        if (entity == null) {
            throw new RuntimeException("Entity is null");
        }
        return departmentRepo.saveAndFlush(entity);
    }

    @Override
    public void delete(String departmentName) {
        departmentRepo.deleteById(departmentName);
    }

    // Add any additional methods if needed
}
