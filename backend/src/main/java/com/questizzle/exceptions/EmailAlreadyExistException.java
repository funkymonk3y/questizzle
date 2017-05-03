package com.questizzle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Danny on 3/24/2017.
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User email already registered")
public class EmailAlreadyExistException extends RuntimeException {

    public EmailAlreadyExistException() {
        super();
    }

}
