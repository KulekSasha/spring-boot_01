package com.nix.repository;

import com.nix.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByNameIgnoreCase(String name);

}
