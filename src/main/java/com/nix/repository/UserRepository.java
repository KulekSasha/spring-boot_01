package com.nix.repository;

import com.nix.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByLoginIgnoreCase(String login);

    User findByEmailIgnoreCase(String email);

}
