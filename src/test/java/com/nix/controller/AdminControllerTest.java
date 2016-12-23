package com.nix.controller;

import com.nix.model.Role;
import com.nix.model.User;
import com.nix.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = AdminController.class, secure = false)
@ActiveProfiles("test")
public class AdminControllerTest {


    @MockBean(name = "userService")
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void admin() throws Exception {
        User user = getExpectedUser();

        given(userService.findByLogin(user.getLogin()))
                .willReturn(user);

        mockMvc.perform(get("/admin/users").principal(user::getLogin))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/admin/admin.jsp"))
                .andDo(result -> {
                    HttpSession session = result.getRequest().getSession();
                    User loginUser = (User) session.getAttribute("loginUser");
                    Assert.assertEquals("user should be equal", loginUser, user);
                });

        verify(userService, times(1)).findByLogin(user.getLogin());
        verifyNoMoreInteractions(userService);
    }

    private User getExpectedUser() {
        return new User(1L, "testUser_1", "testUser_1",
                "testUser_1@gmail.com", "Ivan", "Ivanov",
                new GregorianCalendar(1986, Calendar.JANUARY, 1).getTime(),
                new Role(2L, "Admin"));
    }

}