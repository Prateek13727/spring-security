package com.baeldung.lss.web.controller;

import com.baeldung.lss.model.User;
import com.baeldung.lss.service.UserService;
import com.baeldung.lss.validation.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;

/**
 * Created by tracxn-lp-175 on 23/12/17.
 */

@Controller
public class RegistrationController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/signup")
    public ModelAndView registrationForm() {
        return new ModelAndView("registrationPage", "user", new User());
    }

    @RequestMapping(value = "/register")
    public ModelAndView registerUser(@Valid final User user, final BindingResult result,  final HttpServletRequest request) {
        if (result.hasErrors()) {
            return new ModelAndView("registrationPage", "user", user);
        }
        try {
            userService.registerNewUser(user, request);
        } catch (EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return new ModelAndView("registrationPage", "user", user);
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/registrationConfirm")
    public ModelAndView activateUser(@RequestParam("token") final String token, final RedirectAttributes redirectAttributes) {
        try {
            userService.activateUser(token);
            redirectAttributes.addFlashAttribute("message", "Your account verified successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return new ModelAndView("redirect:/login");
    }


}
