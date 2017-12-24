package com.baeldung.lss.service;

import com.baeldung.lss.model.User;
import com.baeldung.lss.validation.EmailExistsException;

/**
 * Created by tracxn-lp-175 on 23/12/17.
 */
public interface IUserService {

    User registerNewUser(final User user) throws EmailExistsException;
}
