package com.questizzle.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by danny.grullon on 3/18/17.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No data found")
public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String id) {
        super(id + " not found");
    }
}
