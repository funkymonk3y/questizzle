package com.questizzle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Danny on 3/22/2017.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User already exists")
public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException() {
        super();
    }

}
