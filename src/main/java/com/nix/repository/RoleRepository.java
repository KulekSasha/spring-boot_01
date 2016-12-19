package com.nix.repository;

import com.nix.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional(value = Transactional.TxType.MANDATORY)
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}
