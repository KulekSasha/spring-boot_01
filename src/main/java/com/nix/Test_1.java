package com.nix;


import com.nix.model.User;
import com.nix.service.UserService;
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
    public CommandLineRunner demo(UserService userService) {
        return (args) -> {


            User testUser_1 = userService.findByLogin("testUser_1");

            testUser_1.setFirstName("updated");
            testUser_1.setLogin("updated");
            testUser_1.setId(0L);

            userService.update(testUser_1);

        };
    }

}
