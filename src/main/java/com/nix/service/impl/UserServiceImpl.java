package com.nix.service.impl;

import com.nix.model.User;
import com.nix.repository.UserRepository;
import com.nix.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(@Qualifier("userRepository") UserRepository userRepository) {
        log.info("Instantiate {}", this.getClass().getSimpleName());
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        log.trace("Invoke create with param: {}", user);
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User update(User user) {
        log.trace("Invoke update with param: {}", user);
        if (userRepository.findOne(user.getId()) != null) {
            return userRepository.saveAndFlush(user);
        }
        return null;
    }

    @Override
    public void delete(User user) {
        log.trace("Invoke remove with param: {}", user);
        userRepository.delete(user);
    }

    @Override
    public List<User> findAll() {
        log.trace("Invoke findAll");
        return userRepository.findAll();
    }

    @Override
    public User findByLogin(String login) {
        log.debug("Invoke findByLogin with param: {}", login);
        return userRepository.findByLoginIgnoreCase(login);
    }

    @Override
    public User findByEmail(String email) {
        log.debug("Invoke findByEmail with param: {}", email);
        return userRepository.findByEmailIgnoreCase(email);
    }

}
