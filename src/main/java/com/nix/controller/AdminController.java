package com.nix.controller;

import com.nix.model.User;
import com.nix.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private UserService userService;

    @Autowired
    public void setUserService(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public ModelAndView admin(ModelAndView modelAndView,
                              Principal principal,
                              HttpServletRequest req) {

        if (principal != null) {
            log.debug("get users page for user: {}", principal.getName());
            req.getSession().setAttribute("loginUser",
                    userService.findByLogin(principal.getName()));
        }

        modelAndView.setViewName("admin/admin");
        return modelAndView;
    }
}
