package com.nix.api.rest;

import com.nix.api.rest.exception.NotValidUserException;
import com.nix.model.User;
import com.nix.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
        return userService.findAll();
    }

    @RequestMapping(value = "/{login}", method = RequestMethod.GET)
    public ResponseEntity<User> getUserByLogin(@PathVariable String login) {
        User user = userService.findByLogin(login);

        return user != null
                ? ResponseEntity.ok(user)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @RequestMapping(value = "/{login}", method = RequestMethod.PUT)
    public ResponseEntity updateUser(@Valid @RequestBody User user,
                                     BindingResult result,
                                     @PathVariable String login) {

        log.debug("update user, incoming user: {}", user);

        if (userService.findByLogin(login) == null) {
            return ResponseEntity.notFound().build();
        }

        if (!login.equalsIgnoreCase(user.getLogin())) {
            result.rejectValue("login", "login.not.changeable", "login can not be changed");
        }

        if (result.hasErrors()) {
            throw new NotValidUserException("updated user not valid", result);
        }

        userService.update(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{login}")
                .buildAndExpand(user.getLogin()).toUri();

        return ResponseEntity.ok()
                .location(location)
                .build();
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

}
