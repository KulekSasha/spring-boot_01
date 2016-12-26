package com.nix.api.rest.controller;

import com.nix.api.rest.exception.UserNotFoundException;
import com.nix.model.Role;
import com.nix.model.User;
import com.nix.service.RoleService;
import com.nix.service.UserService;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserRestController.class, secure = false)
@ActiveProfiles("test")
public class UserRestControllerTest {

    private static final String BASIC_URL = "/api/rest/users";

    @MockBean(name = "userService")
    private UserService userService;

    @MockBean(name = "roleService")
    private RoleService roleService;

    @Autowired
    private MockMvc mockMvc;

    @Test(timeout = 2000L)
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
                .andExpect(jsonPath("$[0].id", is(((int) userOne.getId()))))
                .andExpect(jsonPath("$[0].login", is(userOne.getLogin())))
                .andExpect(jsonPath("$[0].email", is(userOne.getEmail())))
                .andExpect(jsonPath("$[0].firstName", is(userOne.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(userOne.getLastName())))
                .andExpect(jsonPath("$[0].birthday", is(birthday)))
                .andExpect(jsonPath("$[0].role", is(userOne.getRole().getName())));

        then(userService).should(times(1)).findAll();

        verifyNoMoreInteractions(userService);
    }

    @Test(timeout = 2000L)
    public void getUserByLogin() throws Exception {
        String birthday = "1986-01-01";
        Date date = DateUtils.parseDate(birthday, "yyyy-MM-dd");

        User userOne = new User(1L, "login", "pa$$word", "email@mail",
                "fn", "ln", date, new Role(1L, "Admin"));

        given(userService.findByLogin(userOne.getLogin()))
                .willReturn(userOne);

        mockMvc.perform(get(BASIC_URL + "/" + userOne.getLogin()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(((int) userOne.getId()))))
                .andExpect(jsonPath("$.login", is(userOne.getLogin())))
                .andExpect(jsonPath("$.email", is(userOne.getEmail())))
                .andExpect(jsonPath("$.firstName", is(userOne.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(userOne.getLastName())))
                .andExpect(jsonPath("$.birthday", is(birthday)))
                .andExpect(jsonPath("$.role", is(userOne.getRole().getName())));

        then(userService).should(times(1)).findByLogin(userOne.getLogin());

        verifyNoMoreInteractions(userService);
    }

    @Test(timeout = 2000L)
    public void getUserByInvalidLogin() throws Exception {
        String birthday = "1986-01-01";
        Date date = DateUtils.parseDate(birthday, "yyyy-MM-dd");

        User userOne = new User(1L, "login", "pa$$word", "email@mail",
                "fn", "ln", date, new Role(1L, "Admin"));

        given(userService.findByLogin(anyString()))
                .willReturn(null);

        MvcResult mvcResult = mockMvc.perform(get(BASIC_URL + "/" + userOne.getLogin()))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(isEmptyString()))
                .andReturn();

        assertThat("resolved exception should be equal", mvcResult.getResolvedException(),
                instanceOf(UserNotFoundException.class));

        then(userService).should(times(1)).findByLogin(anyString());

        verifyNoMoreInteractions(userService);
    }

    @Test(timeout = 2000L)
    public void updateUser() throws Exception {
        String birthday = "1986-01-01";
        Date date = DateUtils.parseDate(birthday, "yyyy-MM-dd");

        Role adminRole = new Role(1L, "Admin");
        User expectedUser = new User(1L, "testUser_1", "password", "email@up",
                "fnUp", "lnUp", date, adminRole);

        JSONObject updatedUser = new JSONObject()
                .put("id", expectedUser.getId())
                .put("login", expectedUser.getLogin())
                .put("password", expectedUser.getPassword())
                .put("email", expectedUser.getEmail())
                .put("firstName", expectedUser.getFirstName())
                .put("lastName", expectedUser.getLastName())
                .put("birthday", birthday)
                .put("role", expectedUser.getRole().getName());

        given(userService.update(anyObject())).willReturn(expectedUser);
        given(userService.findByLogin(expectedUser.getLogin()))
                .willReturn(expectedUser);
        given(roleService.findByName(adminRole.getName())).willReturn(adminRole);


        mockMvc.perform(put(BASIC_URL + "/" + expectedUser.getLogin())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(updatedUser.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(updatedUser.toString()))
                .andExpect(header().string("Location", not(isEmptyOrNullString())));


        then(userService).should(times(1)).update(expectedUser);
        then(userService).should(times(1)).findByLogin(expectedUser.getLogin());
        then(roleService).should(times(1)).findByName(adminRole.getName());

        verifyNoMoreInteractions(userService, roleService);
    }

    @Test(timeout = 2000L)
    public void updateUserNotValidData() throws Exception {
        String birthday = "2150-01-01";
        Date date = DateUtils.parseDate(birthday, "yyyy-MM-dd");

        Role adminRole = new Role(1L, "Admin");
        User expectedUser = new User(1L, "testUser_1", "p", "email",
                "f", "l", date, adminRole);

        JSONObject updatedUser = new JSONObject()
                .put("id", expectedUser.getId())
                .put("login", expectedUser.getLogin())
                .put("password", expectedUser.getPassword())
                .put("email", expectedUser.getEmail())
                .put("firstName", expectedUser.getFirstName())
                .put("lastName", expectedUser.getLastName())
                .put("birthday", birthday)
                .put("role", expectedUser.getRole().getName());

        given(userService.findByLogin(expectedUser.getLogin()))
                .willReturn(expectedUser);
        given(roleService.findByName(adminRole.getName())).willReturn(adminRole);


        mockMvc.perform(put(BASIC_URL + "/" + expectedUser.getLogin())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(updatedUser.toString()))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.*", hasSize(5)))
                .andExpect(jsonPath("$.firstName", hasSize(1)))
                .andExpect(jsonPath("$.lastName", hasSize(1)))
                .andExpect(jsonPath("$.password", hasSize(1)))
                .andExpect(jsonPath("$.birthday", hasSize(1)))
                .andExpect(jsonPath("$.email", hasSize(1)));

        then(userService).should(times(1)).findByLogin(expectedUser.getLogin());
        then(roleService).should(times(1)).findByName(adminRole.getName());

        verifyNoMoreInteractions(userService, roleService);
    }

    @Test(timeout = 2000L)
    public void createUser() throws Exception {
        String birthday = "1986-01-01";
        Date date = DateUtils.parseDate(birthday, "yyyy-MM-dd");

        Role adminRole = new Role(1L, "Admin");
        User expectedUser = new User(1L, "testUser_1", "password", "email@new",
                "fnNew", "lnNew", date, adminRole);

        JSONObject newUser = new JSONObject()
                .put("id", expectedUser.getId())
                .put("login", expectedUser.getLogin())
                .put("password", expectedUser.getPassword())
                .put("email", expectedUser.getEmail())
                .put("firstName", expectedUser.getFirstName())
                .put("lastName", expectedUser.getLastName())
                .put("birthday", birthday)
                .put("role", expectedUser.getRole().getName());

        given(userService.create(anyObject())).willReturn(expectedUser);
        given(userService.findByLogin(expectedUser.getLogin()))
                .willReturn(null);
        given(roleService.findByName(adminRole.getName())).willReturn(adminRole);


        mockMvc.perform(post(BASIC_URL)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(newUser.toString()))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(newUser.toString()))
                .andExpect(header().string("Location",
                        endsWith(BASIC_URL + "/" + expectedUser.getLogin())));

        then(userService).should(times(1)).create(expectedUser);
        then(userService).should(times(1)).findByLogin(expectedUser.getLogin());
        then(roleService).should(times(1)).findByName(adminRole.getName());

        verifyNoMoreInteractions(userService, roleService);
    }

    @Test(timeout = 2000L)
    public void deleteUser() throws Exception {

        String login = "toDelete";

        User userToDelete = new User();
        userToDelete.setLogin(login);

        given(userService.findByLogin(login)).willReturn(userToDelete);

        mockMvc.perform(delete(BASIC_URL + "/" + login)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isNoContent());

        then(userService).should(times(1)).findByLogin(login);
        then(userService).should(times(1)).delete(anyObject());

        verifyNoMoreInteractions(userService, roleService);
    }

}