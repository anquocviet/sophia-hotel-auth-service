package vn.edu.iuh.authservice.exceptions.impl;

import org.apache.http.HttpStatus;
import vn.edu.iuh.authservice.exceptions.AppException;

/**
 * @description Exception thrown when user credentials are invalid
 * @author: vie
 * @date: 14/2/25
 */
public class InvalidCredentialsException extends AppException {
    public InvalidCredentialsException() {
        super(HttpStatus.SC_UNAUTHORIZED, "Invalid username or password");
    }
}
