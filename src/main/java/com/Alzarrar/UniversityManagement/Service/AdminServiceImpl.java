package com.Alzarrar.UniversityManagement.Service;

import com.Alzarrar.UniversityManagement.Entity.AdminEntity;
import com.Alzarrar.UniversityManagement.repositry.AdminRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepo adminRepo;
    @Override
    public List<AdminEntity> findAll() {
        return adminRepo.findAll();
    }
    @Override
    public AdminEntity findByEmail(String email) {
        return adminRepo.getByEmailIgnoreCase(email);
    }
    @Override
    public Boolean existsByEmail(String email) {
        return adminRepo.existsByEmailIgnoreCase(email);
    }
    @Override
    public List<AdminEntity> findAllByRole(String role) {
        return adminRepo.findAllByRoleIgnoreCase(role);
    }
    @Override
    public List<AdminEntity> findAllByNameOrCity(String name, String city) {
        return adminRepo.findAllByNameIgnoreCaseOrCityIgnoreCase(name, city);
    }
    @Override
    public AdminEntity save(AdminEntity entity) {
        if (entity == null) {
            throw new RuntimeException("Entity is null");
        }
        return adminRepo.saveAndFlush(entity);
    }
    @Override
    public void delete(Integer adminId) {
        adminRepo.deleteById(adminId);
    }
    @Override
    public Boolean existsByUsername(String username) {
        return adminRepo.existsByUsernameIgnoreCase(username);
    }
    // Add any additional methods if needed
    @Override
    public void deleteByName(String name) {
        adminRepo.deleteByName(name);
    }


}
