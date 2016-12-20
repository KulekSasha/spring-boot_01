package com.nix.repository;

import com.nix.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(value = "user.role", type = EntityGraph.EntityGraphType.LOAD)
    User findByLoginIgnoreCase(String login);

    @EntityGraph(value = "user.role", type = EntityGraph.EntityGraphType.LOAD)
    User findByEmailIgnoreCase(String email);

}
