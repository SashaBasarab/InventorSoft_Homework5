package com.example.inventorsoft_homework5.repository;

import com.example.inventorsoft_homework5.entity.Role;
import com.example.inventorsoft_homework5.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(UserRole name);

}
