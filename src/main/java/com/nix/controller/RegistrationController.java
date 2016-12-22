package com.nix.controller;


import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;
import com.github.mkopylec.recaptcha.validation.ValidationResult;
import com.nix.model.Role;
import com.nix.model.User;
import com.nix.service.RoleService;
import com.nix.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class RegistrationController {

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);
    private static final int DATE_LENGTH = 10;

    private UserService userService;
    private RoleService roleService;
    private RecaptchaValidator recaptchaValidator;


    @Autowired
    public void setUserService(@Qualifier("userService") UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRoleService(@Qualifier("roleService") RoleService roleService) {
        this.roleService = roleService;
    }

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    public void setReCaptcha(RecaptchaValidator recaptchaValidator) {
        this.recaptchaValidator = recaptchaValidator;
    }

    @InitBinder
    public void bindingPreparation(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);

        CustomDateEditor birthdayEditor = new CustomDateEditor(dateFormat, true, DATE_LENGTH);
        binder.registerCustomEditor(Date.class, "birthday", birthdayEditor);
        binder.registerCustomEditor(Role.class, "role", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text != null) {
                    Role role = roleService.findByName(text);
                    setValue(role);
                    return;
                }
                throw new IllegalArgumentException("argument is null");
            }
        });
    }

    @RequestMapping(value = "registration/new", method = RequestMethod.GET)
    public ModelAndView registerNewUser(ModelAndView modelAndView) {

        log.debug("get form for new user registration");

        modelAndView.addObject("newUser", new User());
        modelAndView.setViewName("registration/registration");
        return modelAndView;
    }

    @RequestMapping(value = "registration/new", method = RequestMethod.POST)
    public ModelAndView registerNewUserPost(ModelAndView modelAndView,
                                            @Valid @ModelAttribute("newUser") User newUser,
                                            BindingResult result,
                                            @RequestParam("passConfirm") String passConfirm,
                                            HttpServletRequest request) {

        if (!newUser.getPassword().equals(passConfirm)) {
            result.rejectValue("password", "NotEqual.password", "passwords do not match");
        }

        if (userService.findByLogin(newUser.getLogin()) != null) {
            result.rejectValue("login", "non.unique.login", "login exist");
        }

        log.debug("registerNewUserPost - ModelAttribute - newUser: {}", newUser);
        log.debug("registerNewUserPost - BindingResult - errors: {}", result);

        ValidationResult reCaptchaResult = recaptchaValidator.validate(request);

        if (!reCaptchaResult.isSuccess()) {
            modelAndView.addObject("invalidRecaptcha", true);
        }

        if (result.hasErrors() || !reCaptchaResult.isSuccess()) {
            modelAndView.addObject("errors", result);
            modelAndView.setViewName("registration/registration");
            return modelAndView;
        }

        userService.create(newUser);
        modelAndView.setViewName("login");
        return modelAndView;
    }
}
