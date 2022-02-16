package com.venturedive.login.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.venturedive.login.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

    Role findByName(String name);
}
