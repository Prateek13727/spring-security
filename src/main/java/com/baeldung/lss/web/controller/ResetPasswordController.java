package com.baeldung.lss.web.controller;

import com.baeldung.lss.service.UserService;
import com.google.common.collect.ImmutableMap;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by tracxn-lp-175 on 3/1/18.
 */
@Controller
public class ResetPasswordController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView resetPassword(final HttpServletRequest request, @RequestParam("email") final String userEmail, final RedirectAttributes redirectAttributes) {
        userService.resetPasssword(userEmail, request);
        redirectAttributes.addFlashAttribute("message", "You should receive an Password Reset Email shortly");
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public ModelAndView showChangePasswordPage(@RequestParam("id") final long id, @RequestParam("token") final String token, final RedirectAttributes redirectAttributes) {
        try {
            userService.changePassword(token, id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("Error Message", e.getMessage());
            return new ModelAndView("redirect:/login");
        }
        return new ModelAndView("resetPassword");
    }

    @RequestMapping(value = "/savePassword", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView savePassword(@RequestParam("password") final String password, @RequestParam("passwordConfirmation") final String passwordConfirmation, final RedirectAttributes redirectAttributes) {
        try {
            userService.savePassword(password, passwordConfirmation);
            redirectAttributes.addFlashAttribute("message", "Password reset successfully");
        } catch (Exception e) {
            return new ModelAndView("resetPassword", ImmutableMap.of("errorMessage", e.getMessage()));
        }
        return new ModelAndView("redirect:/login");
    }

}
