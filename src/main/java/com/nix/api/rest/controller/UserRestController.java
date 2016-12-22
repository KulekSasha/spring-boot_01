package com.nix.api.rest.controller;

import com.nix.api.rest.exception.NotValidUserException;
import com.nix.api.rest.exception.UserNotFoundException;
import com.nix.model.User;
import com.nix.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/rest/users",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestController {

    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);

    private final UserService userService;

    @Autowired
    public UserRestController(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> getUsers() {
        log.debug("invoke getUsers");
        return userService.findAll();
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.GET)
    public User getUserByLogin(@PathVariable String login) {

        log.debug("invoke getUserByLogin, incoming login: {}", login);

        User user = userService.findByLogin(login);

        if (user == null) {
            log.error("user not found, login: {}", login);
            throw new UserNotFoundException("user login: " + login);
        }

        log.debug("return found user: {}", user);
        return user;
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.PUT)
    public ResponseEntity updateUser(@Valid @RequestBody User user,
                                     BindingResult result,
                                     @PathVariable String login) {

        log.debug("invoke updateUser, incoming user: {}", user);

        if (userService.findByLogin(login) == null) {
            log.error("user for update not found, login: {}", login);
            throw new UserNotFoundException("user login: " + login);
        }

        if (!login.equalsIgnoreCase(user.getLogin())) {
            result.rejectValue("login", "login.not.changeable", "login can not be changed");
        }

        if (result.hasErrors()) {
            log.debug("user for update not valid, errors: {}", result.getFieldErrors());
            throw new NotValidUserException("updated user not valid", result);
        }

        User updatedUser = userService.update(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{login}")
                .buildAndExpand(updatedUser.getLogin()).toUri();

        return ResponseEntity.ok()
                .location(location)
                .body(updatedUser);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createUser(@Valid @RequestBody User user,
                                     BindingResult result) {

        log.debug("update user, incoming user: {}", user);

        if (userService.findByLogin(user.getLogin()) != null) {
            result.rejectValue("login", "non.unique.login", "login exist");
        }

        if (result.hasErrors()) {
            throw new NotValidUserException("new user not valid", result);
        }

        User createdUser = userService.create(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{login}")
                .buildAndExpand(user.getLogin()).toUri();

        return ResponseEntity
                .created(location)
                .body(createdUser);
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable String login) {
        log.debug("invoke deleteUser with login: {}", login);

        User userToDelete = userService.findByLogin(login);

        if (userToDelete == null) {
            log.error("user for deleting not found, login: {}", login);
            throw new UserNotFoundException("User not found");
        }

        userService.delete(userToDelete);
        log.debug("user deleted, login: {}", login);
        return ResponseEntity.noContent().build();
    }

}
