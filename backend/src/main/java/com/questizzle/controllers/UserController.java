package com.questizzle.controllers;

import com.questizzle.models.User;
import com.questizzle.security.utils.JwtTokenUtil;
import com.questizzle.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by Danny on 3/20/2017.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    public static final String tokenHeader = "Authorization";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        List<User> users = userService.getAllUsers();

        for(int i = 0; i < users.size(); i++) {
            String id = users.get(i).getIdentity();
            users.get(i).add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
        }

        return userService.getAllUsers();
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public User getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        User user = userService.getUser(username);

        String id = user.getIdentity();
        user.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());

        return user;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody User user, HttpServletRequest request) {
        String baseUrl = String.format("%s://%s:%d",request.getScheme(),  request.getServerName(), request.getServerPort());

        User registeredUser = userService.createUser(user, baseUrl);

        String id = registeredUser.getIdentity();
        registeredUser.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());

        return registeredUser;
    }

    @RequestMapping(value = "/confirm/{user_id}/{confirmToken}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String confirmEmailAccount(@PathVariable("user_id") String id, @PathVariable("confirmToken") String token) {
        if(userService.doesTokenMatch(id, token)) {
            return "Success";
        } else {
            return "Failure";
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("id") String id) {
        User user = userService.getUserById(id);
        user.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());

        return user;
    }

    @RequestMapping(value = "/info/{username}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public User getUserInfo(@PathVariable("username") String username) {
        User user = userService.getUser(username);
        user.add(linkTo(methodOn(UserController.class).getUser(user.getIdentity())).withSelfRel());

        return user;
    }
}
