package com.nix.controller;

import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import com.nix.model.Role;
import com.nix.model.User;
import com.nix.service.RoleService;
import com.nix.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = RegistrationController.class, secure = false)
@ActiveProfiles("test")
public class RegistrationControllerTest {

    @MockBean(name = "userService")
    private UserService userService;
    @MockBean(name = "roleService")
    private RoleService roleService;
    @MockBean
    private RecaptchaValidator recaptchaValidator;

    @Autowired
    private MockMvc mockMvc;

    @Test(timeout = 2000L)
    public void registerNewUser() throws Exception {
        User userModel = new User();

        mockMvc.perform(get("/registration/new"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("newUser", userModel))
                .andExpect(view().name("registration/registration"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/registration/registration.jsp"));
    }

    @Test(timeout = 2000L)
    public void registerNewUserPost() throws Exception {
        MultiValueMap<String, String> paramMap = getValidPostParams();
        Role expectedRole = new Role(2L, "User");

        given(roleService.findByName(paramMap.getFirst("role")))
                .willReturn(expectedRole);

        given(recaptchaValidator.validate(any(HttpServletRequest.class)))
                .willReturn(new ValidationResult(true, null));

        mockMvc.perform(post("/registration/new").params(paramMap))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/login.jsp"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("newUser"));

        then(userService).should(times(1)).create(any(User.class));
        then(userService).should(times(1)).findByLogin(any(String.class));
        then(roleService).should(times(1)).findByName(paramMap.getFirst("role"));
        then(recaptchaValidator).should(times(1)).validate(any(HttpServletRequest.class));

        verifyNoMoreInteractions(userService, roleService, recaptchaValidator);
    }

    @Test(timeout = 2000L)
    public void registerNewUserPost_NotValidData() throws Exception {
        MultiValueMap<String, String> paramMap = getValidPostParams();
        Role expectedRole = new Role(2L, "User");
        String emptyString = "";

        paramMap.set("passConfirm", emptyString);

        given(roleService.findByName(paramMap.getFirst("role")))
                .willReturn(expectedRole);

        given(recaptchaValidator.validate(any(HttpServletRequest.class)))
                .willReturn(new ValidationResult(true, null));

        mockMvc.perform(post("/registration/new").params(paramMap))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("registration/registration"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/registration/registration.jsp"))
                .andExpect(model().attributeHasFieldErrors("newUser", "password"))
                .andExpect(model().attributeExists("errors"));

        then(userService).should(times(1)).findByLogin(any(String.class));
        then(roleService).should(times(1)).findByName(paramMap.getFirst("role"));
        then(recaptchaValidator).should(times(1)).validate(any(HttpServletRequest.class));

        verifyNoMoreInteractions(userService, roleService, recaptchaValidator);
    }

    private MultiValueMap<String, String> getValidPostParams() {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("login", "testLogin");
        paramMap.add("password", "testPassword");
        paramMap.add("passConfirm", "testPassword");
        paramMap.add("email", "test@mail.test");
        paramMap.add("firstName", "testFirstName");
        paramMap.add("lastName", "testLastName");
        paramMap.add("birthday", "2016-03-25");
        paramMap.add("role", "User");
        paramMap.add("recaptcha_challenge_field", "challenge");
        paramMap.add("recaptcha_response_field", "response");
        return paramMap;
    }

}