package com.nix.controller;

import com.nix.model.Role;
import com.nix.model.User;
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

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test(timeout = 2000L)
    public void showUserPage() throws Exception {
        User user = getDefaultUser();
        given(userService.findByLogin(user.getLogin())).willReturn(user);

        mockMvc.perform(get("/user/user").principal(() -> user.getLogin()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(model().attribute("user", user))
                .andExpect(view().name("user/user"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/user/user.jsp"));

        then(userService).should(times(1)).findByLogin(user.getLogin());
    }

    private User getDefaultUser() {
        return new User(1L, "testUser_1", "testUser_1",
                "testUser_1@gmail.com", "Ivan", "Ivanov",
                new GregorianCalendar(1986, Calendar.JANUARY, 1).getTime(),
                new Role(2L, "Admin"));
    }
}