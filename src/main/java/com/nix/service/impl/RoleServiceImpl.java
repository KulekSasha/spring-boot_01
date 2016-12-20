package com.nix.service.impl;

import com.nix.model.Role;
import com.nix.repository.RoleRepository;
import com.nix.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("roleService")
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(@Qualifier("roleRepository") RoleRepository roleRepository) {
        log.info("Instantiate {}", this.getClass().getSimpleName());
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public Role create(Role role) {
        log.trace("Invoke create with param: {}", role);
        return roleRepository.saveAndFlush(role);
    }

    @Override
    @Transactional
    public Role update(Role role) {
        log.trace("Invoke update with param: {}", role);
        if (roleRepository.findOne(role.getId()) != null) {
            return roleRepository.saveAndFlush(role);
        }
        return null;
    }

    @Override
    @Transactional
    public void delete(Role role) {
        log.trace("Invoke remove with param: {}", role);
        roleRepository.delete(role);
    }

    @Override
    public Role findByName(String name) {
        log.trace("Invoke findByName with param: {}", name);
        return roleRepository.findByNameIgnoreCase(name);
    }
}
