package com.baeldung.lss.service;

import com.baeldung.lss.event.OnRegistrationCompleteEvent;
import com.baeldung.lss.model.VerificationToken;
import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.model.User;
import com.baeldung.lss.persistence.VerificationTokenRepository;
import com.baeldung.lss.validation.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * Created by tracxn-lp-175 on 23/12/17.
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public User registerNewUser(final User user, HttpServletRequest request) throws EmailExistsException {
        if (emailExist(user.getEmail())) {
            throw new EmailExistsException("There is an account with that email address: " + user.getEmail());
        }
        user.setEnabled(false);
        User newUser = repository.save(user);
        String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(newUser, appUrl));
        return newUser;
    }

    @Override
    public void activateUser(String token) throws  Exception {
        final VerificationToken verificationToken = getVerificationToken(token);
        if (verificationToken == null) {
            throw new Exception("Invalid account confirmation token.");
        }
        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new Exception("Your registration token has expired. Please register again.");
        }
        user.setEnabled(true);
        repository.save(user);
    }

    @Override
    public void createNewVerificationToken(User user, String token) {
        final VerificationToken newToken = new VerificationToken(token, user);
        tokenRepository.save(newToken);
    }

    private VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    private boolean emailExist(String email) {
        final User user = repository.findByEmail(email);
        return user != null;
    }

}
