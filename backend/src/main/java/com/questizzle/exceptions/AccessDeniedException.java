package com.questizzle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Danny on 4/26/2017.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Access denied")
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super();
    }
}
