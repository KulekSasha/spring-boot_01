package com.nix;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(Application.class);
        return application;
    }

//    public static void main(String[] args) throws Exception {
//        SpringApplication app = new SpringApplication(Application.class);
//        app.setBannerMode(Banner.Mode.OFF);
//        app.run(args);
//    }

//    @Bean
//    public CommandLineRunner demo(UserService userService) {
//        return (args) -> {
//            User testUser_1 = userService.findByLogin("testUser_1");
//            testUser_1.setFirstName("updated_88");
//            userService.update(testUser_1);
//        };
//    }


}
