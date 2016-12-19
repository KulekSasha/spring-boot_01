package com.nix.service;

import com.nix.model.Role;

public interface RoleService {

    Role create(Role role);

    Role update(Role role);

    void delete(Role role);

    Role findByName(String name);

}
