package com.baeldung.lss.service;

import com.baeldung.lss.event.OnRegistrationCompleteEvent;
import com.baeldung.lss.model.PasswordResetToken;
import com.baeldung.lss.model.VerificationToken;
import com.baeldung.lss.persistence.PasswordResetTokenRepository;
import com.baeldung.lss.persistence.UserRepository;
import com.baeldung.lss.model.User;
import com.baeldung.lss.persistence.VerificationTokenRepository;
import com.baeldung.lss.validation.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialException;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by tracxn-lp-175 on 23/12/17.
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    private Environment env;

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
    public void resetPasssword(String email, HttpServletRequest request) {
        final User user = repository.findByEmail(email);
        if (user != null) {
            final String token = UUID.randomUUID().toString();
            createPasswordResetTokenForUser(user, token);
            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            final SimpleMailMessage emailTemplate = constructResetTokenEmail(appUrl, token, user);
            mailSender.send(emailTemplate);
        }
    }

    @Override
    public void changePassword(String token, Long id) throws  Exception {
        final PasswordResetToken passToken = getPasswordResetToken(token);
        if (passToken == null) {
            throw new Exception("Invalid password reset token");
        }
        final User user = passToken.getUser();
        if (user.getId() != id) {
            throw new Exception("Invalid password reset token");
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new Exception("Your password reset token has expired");
        }
        final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, userDetailsService.loadUserByUsername(user.getEmail()).getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public void savePassword(String password, String passwordConfirmation) throws Exception {
        if (!password.equals(passwordConfirmation)) {
            throw new Exception("Passwords do not match");
        }
        final User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        changeUserPassword(user, password);
    }

    @Override
    public void createNewVerificationToken(User user, String token) {
        final VerificationToken newToken = new VerificationToken(token, user);
        verificationTokenRepository.save(newToken);
    }

    private VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    private boolean emailExist(String email) {
        final User user = repository.findByEmail(email);
        return user != null;
    }

    private void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    private PasswordResetToken getPasswordResetToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }

    private void changeUserPassword(User user, String password) {
        user.setPassword(password);
        repository.save(user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final String token, final User user) {
        final String url = contextPath + "/changePassword?id=" + user.getId() + "&token=" + token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Reset Password");
        email.setText("Please open the following URL to reset your password: \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

}
