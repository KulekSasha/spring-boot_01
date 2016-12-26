package com.nix.api.rest.controller;

import com.jayway.jsonpath.JsonPath;
import com.nix.model.Role;
import com.nix.model.User;
import com.nix.repository.UserRepository;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureJsonTesters
public class UserRestControllerIT {

    private static final String BASIC_URL = "/api/rest/users";
    private static final String LOGIN = "testUser_1";
    private static final String PASSWORD = "testUser_1";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;


    @Before
    public void setUp() throws Exception {
        restTemplate.getRestTemplate().setInterceptors(asList((request, body, execution) -> {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            request.getHeaders().setAccept(asList(MediaType.APPLICATION_JSON));
            return execution.execute(request, body);
        }));
    }

    @Test(timeout = 10_000L)
    public void getUsers() throws Exception {

//        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth(LOGIN, PASSWORD)
//                .getForEntity(BASIC_URL, String.class);

        ResponseEntity<String> responseEntity =
                restTemplate.withBasicAuth(LOGIN, PASSWORD)
                        .exchange(BASIC_URL, HttpMethod.GET, null, String.class);

        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));

        List<String> logins = JsonPath.read(responseEntity.getBody(), "$.[*].login");
        System.out.println(logins);

        assertThat(logins, hasSize(5));
        assertThat(logins, contains("testUser_1", "testUser_2", "testUser_3",
                "testUser_4", "testUser_5"));
    }

    @Test(timeout = 10_000L)
    public void getUserByLogin() throws Exception {

        User expectedUser = getExpectedUser();

        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth(LOGIN, PASSWORD)
                .getForEntity(BASIC_URL + "/" + expectedUser.getLogin(), String.class);
        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));

//        User user = responseEntity.getBody();
//        assertEquals("users should be equals", user, expectedUser);
    }

    @Test(timeout = 10_000L)
    public void getUserByInvalidLogin() throws Exception {
        String notValidLogin = "notValidLogin";

        ResponseEntity<String> responseEntity = restTemplate.withBasicAuth(LOGIN, PASSWORD)
                .getForEntity(BASIC_URL + "/" + notValidLogin, String.class);

        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.NOT_FOUND));
    }

    @Test(timeout = 10_000L)
    public void updateUser() throws Exception {
        String birthday = "1986-01-01";
        Date date = DateUtils.parseDate(birthday, "yyyy-MM-dd");
        User expectedUser = new User(1L, "testUser_1", "testUser_1", "email@up",
                "fnUp", "lnUp", date, new Role(1L, "ADMIN"));

        JSONObject jsonUser = new JSONObject()
                .put("id", expectedUser.getId())
                .put("login", expectedUser.getLogin())
                .put("password", expectedUser.getPassword())
                .put("email", expectedUser.getEmail())
                .put("firstName", expectedUser.getFirstName())
                .put("lastName", expectedUser.getLastName())
                .put("birthday", birthday)
                .put("role", expectedUser.getRole().getName());

        ResponseEntity<String> responseEntity =
                restTemplate.withBasicAuth(LOGIN, PASSWORD)
                        .exchange(BASIC_URL + "/" + expectedUser.getLogin(), HttpMethod.PUT,
                                new HttpEntity<>(jsonUser.toString()), String.class);

        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.OK));
        JSONAssert.assertEquals(jsonUser.toString(), responseEntity.getBody(), JSONCompareMode.STRICT);

        User persistedUser = userRepository.findOne(1L);

        assertThat("users should be equal", persistedUser,
                is(expectedUser));
    }

    //    @Test(timeout = 2000L)
//    public void updateUserNotValidData() throws Exception {
//        String birthday = "2150-01-01";
//        Date date = DateUtils.parseDate(birthday, "yyyy-MM-dd");
//
//        Role adminRole = new Role(1L, "Admin");
//        User expectedUser = new User(1L, "testUser_1", "p", "email",
//                "f", "l", date, adminRole);
//
//        JSONObject updatedUser = new JSONObject()
//                .put("id", expectedUser.getId())
//                .put("login", expectedUser.getLogin())
//                .put("password", expectedUser.getPassword())
//                .put("email", expectedUser.getEmail())
//                .put("firstName", expectedUser.getFirstName())
//                .put("lastName", expectedUser.getLastName())
//                .put("birthday", birthday)
//                .put("role", expectedUser.getRole().getName());
//
//        given(userService.findByLogin(expectedUser.getLogin()))
//                .willReturn(expectedUser);
//        given(roleService.findByName(adminRole.getName())).willReturn(adminRole);
//
//
//        mockMvc.perform(put(BASIC_URL + "/" + expectedUser.getLogin())
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .content(updatedUser.toString()))
//                .andDo(print())
//                .andExpect(status().isUnprocessableEntity())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(jsonPath("$.*", hasSize(5)))
//                .andExpect(jsonPath("$.firstName", hasSize(1)))
//                .andExpect(jsonPath("$.lastName", hasSize(1)))
//                .andExpect(jsonPath("$.password", hasSize(1)))
//                .andExpect(jsonPath("$.birthday", hasSize(1)))
//                .andExpect(jsonPath("$.email", hasSize(1)));
//
//        then(userService).should(times(1)).findByLogin(expectedUser.getLogin());
//        then(roleService).should(times(1)).findByName(adminRole.getName());
//
//        verifyNoMoreInteractions(userService, roleService);
//    }
//
    @Test(timeout = 10_000L)
    public void createUser() throws Exception {
        String birthday = "2000-05-01";
        Date date = DateUtils.parseDate(birthday, "yyyy-MM-dd");
        User newUser = new User(0L, "newLogin", "newPass", "email@new",
                "fnNew", "lnNew", date, new Role(2L, "USER"));
        long rowsBefore = userRepository.count();

        JSONObject jsonUser = new JSONObject()
                .put("id", newUser.getId())
                .put("login", newUser.getLogin())
                .put("password", newUser.getPassword())
                .put("email", newUser.getEmail())
                .put("firstName", newUser.getFirstName())
                .put("lastName", newUser.getLastName())
                .put("birthday", birthday)
                .put("role", newUser.getRole().getName());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonUser.toString(), headers);

        ResponseEntity<String> responseEntity =
                restTemplate.withBasicAuth(LOGIN, PASSWORD)
                        .exchange(BASIC_URL, HttpMethod.POST, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.CREATED));

        JSONObject jsonResponse = new JSONObject(responseEntity.getBody());
        long id = jsonResponse.getLong("id");
        jsonUser.put("id", id);
        JSONAssert.assertEquals(jsonUser.toString(), responseEntity.getBody(), JSONCompareMode.STRICT);

        long rowsAfter = userRepository.count();
        assertThat(rowsBefore + 1L, equalTo(rowsAfter));

        User createdUser = userRepository.findOne(id);
        newUser.setId(id);
        assertThat("users should be equal", createdUser,
                is(newUser));

    }

    @Test(timeout = 10_000L)
    public void deleteUser() throws Exception {

        String login = "testUser_4";
        String emptyString = "";
        long rowsBefore = userRepository.count();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(emptyString, headers);

        ResponseEntity<String> responseEntity =
                restTemplate.withBasicAuth(LOGIN, PASSWORD)
                        .exchange(BASIC_URL + "/" + login, HttpMethod.DELETE, httpEntity, String.class);

        assertTrue(responseEntity.getStatusCode().equals(HttpStatus.NO_CONTENT));

        long rowsAfter = userRepository.count();
        assertThat(rowsBefore - rowsAfter, equalTo(1L));

    }

    private User getExpectedUser() {
        return new User(1L, "testUser_1", "testUser_1",
                "testUser_1@gmail.com", "Ivan", "Ivanov",
                new GregorianCalendar(1986, Calendar.JANUARY, 1).getTime(),
                new Role(2L, "ADMIN"));
    }
}