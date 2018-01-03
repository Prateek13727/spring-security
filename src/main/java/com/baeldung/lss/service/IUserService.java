package com.baeldung.lss.service;

import com.baeldung.lss.model.User;
import com.baeldung.lss.validation.EmailExistsException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by tracxn-lp-175 on 23/12/17.
 */
public interface IUserService {

    User registerNewUser(final User user, HttpServletRequest request) throws EmailExistsException;

    void activateUser(String token) throws  Exception;

    void resetPasssword(String email, HttpServletRequest request);

    void changePassword(String token, Long id) throws  Exception;

    void savePassword(String password, String passwordConfirmation) throws Exception;

    void createNewVerificationToken(User user, String token);
}
