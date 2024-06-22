package com.Alzarrar.UniversityManagement.Service;

import com.Alzarrar.UniversityManagement.Entity.AdminEntity;

import java.util.List;

public interface AdminService {

    List<AdminEntity> findAll();

    AdminEntity findByEmail(String email);

    Boolean existsByEmail(String email);

    List<AdminEntity> findAllByRole(String role);

    List<AdminEntity> findAllByNameOrCity(String name, String city);

    AdminEntity save(AdminEntity entity);

    void delete(Integer adminId);
    Boolean existsByUsername(String username);
    void deleteByName(String name);


}
