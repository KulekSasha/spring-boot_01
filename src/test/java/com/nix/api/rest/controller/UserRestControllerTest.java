package com.nix.api.rest.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRestControllerTest {

    private static final String LOGIN = "testUser_1";
    private static final String PASSWORD = "testUser_1";
    private static final String BASIC_URL = "/api/rest/users";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getUsers() throws Exception {

        ResponseEntity<List> responseEntity = restTemplate.withBasicAuth(LOGIN, PASSWORD)
                .getForEntity(BASIC_URL, List.class);

        System.out.println(responseEntity.getBody());
    }

    @Test
    public void getUserByLogin() throws Exception {

    }

    @Test
    public void updateUser() throws Exception {

    }

    @Test
    public void createUser() throws Exception {

    }

    @Test
    public void deleteUser() throws Exception {

    }

}