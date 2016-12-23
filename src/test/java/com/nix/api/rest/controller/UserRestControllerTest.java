package com.nix.api.rest.controller;

import com.nix.model.Role;
import com.nix.model.User;
import com.nix.service.UserService;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserRestController.class, secure = false)
@ActiveProfiles("test")
public class UserRestControllerTest {

    private static final String BASIC_URL = "/api/rest/users";

    @MockBean(name = "userService")
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getUsers() throws Exception {
        String birthday = "1986-01-01";
        Date date = DateUtils.parseDate(birthday, "yyyy-MM-dd");

        User userOne = new User(1L, "login", "pa$$word", "email@mail",
                "fn", "ln", date, new Role(1L, "Admin"));
        User userTwo = new User(2L, "login2", "pa$$word2", "email@mail2",
                "fn2", "ln2", date, new Role(1L, "Admin"));

        given(userService.findAll()).willReturn(Arrays.asList(userOne, userTwo));

        mockMvc.perform(get(BASIC_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(userOne.getId())))
                .andExpect(jsonPath("$[0].id", greaterThanOrEqualTo(userOne.getId())))
                .andExpect(jsonPath("$[0].birthday", is(birthday)));

        then(userService).should(times(1)).findAll();

        verifyNoMoreInteractions(userService);
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