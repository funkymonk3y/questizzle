package com.questizzle.services;

import com.questizzle.exceptions.DataNotFoundException;
import com.questizzle.models.Question;
import com.questizzle.models.User;
import com.questizzle.exceptions.EmailAlreadyExistException;
import com.questizzle.exceptions.UserAlreadyExistException;
import com.questizzle.repositories.UserRepository;
import com.questizzle.utils.MailClient;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Danny on 3/20/2017.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private MailContentBuilder mailContentBuilder;

    @Autowired
    private MongoTemplate mongoTemplate;

    public UserService() {}

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();

        for(int i = 0; i < users.size(); i++) {
            users.get(i).setPassword(null);
        }

        return users;
    }

    public User getUserById(String id) throws DataNotFoundException {
        User user = userRepository.findOne(id);

        if(user == null) {
            throw new DataNotFoundException(id);
        }

        return user;
    }

    public User getUser(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

    public User createUser(User user, String baseUrl) throws RuntimeException {
        User existingUserByUsername = userRepository.findByUsername(user.getUsername());

        if(existingUserByUsername != null) {
            throw new UserAlreadyExistException();
        }

        User existingUserByEmail = userRepository.findByEmail(user.getEmail());

        if(existingUserByEmail != null) {
            throw new EmailAlreadyExistException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmationToken(RandomStringUtils.randomAlphanumeric(35));
        user.setEmailConfirmed(false);
        user.setAccountEnabled(false);
        user.setDateModified(new Date());
        user.setDateCreated(new Date());
        user.setLastPasswordResetDate(new Date());
        user.addRole("ROLE_DEFAULT_USER");

        User savedUser = userRepository.save(user);

        sendConfirmationMail(user.getIdentity(), user.getEmail(), user.getConfirmationToken(), baseUrl);

        return savedUser;
    }

    private void sendConfirmationMail(String userId, String toEmail, String confirmationToken, String baseUrl) {
        String confirmLink = baseUrl + "/register/confirm/" + userId + "/" + confirmationToken;

        String fromEmail = "questizzle9@gmail.com";
        String subject   = "Email Confirmation";
        String bodyMsg   = mailContentBuilder.buildConfirmationEmail(confirmLink);

        mailClient.sendMail(fromEmail, toEmail, subject, bodyMsg);
    }

    public boolean doesTokenMatch(String userId, String confirmationToken) {
        User user = userRepository.findByIdentityAndConfirmationTokenAndIsEmailConfirmed(userId, confirmationToken, false);

        if(user != null) {
            user.setEmailConfirmed(true);
            user.setAccountEnabled(true);
            userRepository.save(user);

            return true;
        } else {
            return false;
        }
    }

    public void incrementCount(String username, String property) {
        User user = userRepository.findByUsername(username);

        Query query = new Query();
        query.addCriteria(Criteria.where("identity").is(user.getIdentity()));

        Update update = new Update();
        update.inc(property, 1);

        mongoTemplate.findAndModify(query, update, User.class);
    }
}
