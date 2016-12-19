package com.nix.repository;

import com.nix.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional(propagation = Propagation.MANDATORY)
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional(readOnly = true)
    @EntityGraph(value = "user.role", type = EntityGraph.EntityGraphType.LOAD)
    User findByLoginIgnoreCase(String login);

    @Transactional(readOnly = true)
    @EntityGraph(value = "user.role", type = EntityGraph.EntityGraphType.LOAD)
    User findByEmailIgnoreCase(String email);


    //    @EntityGraph(value = "user.role", type = EntityGraph.EntityGraphType.LOAD)
//    @Query("select u from User u where LENGTH(u.login) > 1")
//    List<User> myCustomQuery();
//
//    @EntityGraph(value = "user.role", type = EntityGraph.EntityGraphType.LOAD)
//    @Query("select u from User u")
//    List<ProjUser> findAllProj();

}
