package com.nix.service;

import com.nix.model.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User update(User user);

    void delete(User user);

    List<User> findAll();

    User findByLogin(String login);

    User findByEmail(String email);

}
