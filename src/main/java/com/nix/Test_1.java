package com.nix;

import com.nix.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EntityScan(basePackages = "com.nix.model")
public class Test_1 {

    private static final Logger log = LoggerFactory.getLogger(Test_1.class);

    public Test_1() {
        log.debug("initialize constructor");
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Test_1.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository userDao) {
        return (args) -> {
            log.info("find by login: {}", userDao.findByLoginIgnoreCase("tEstUser_1"));
            log.info("find all: {}", userDao.findAll());
            log.info("find by email: {}", userDao.findByEmail("tEstUser_2@gmail.com"));
            log.info("count: {}", userDao.count());
        };
    }
}
