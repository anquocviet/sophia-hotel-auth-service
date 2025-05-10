package vn.edu.iuh.authservice.exceptions.impl;

import org.apache.http.HttpStatus;
import vn.edu.iuh.authservice.exceptions.AppException;

/**
 * @description Exception thrown when password validation fails
 * @author: vie
 * @date: 14/2/25
 */
public class InvalidPasswordException extends AppException {
    public InvalidPasswordException() {
        super(HttpStatus.SC_BAD_REQUEST, "Old password is incorrect");
    }
    
    public InvalidPasswordException(String message) {
        super(HttpStatus.SC_BAD_REQUEST, message);
    }
}
