package vn.edu.iuh.authservice.exceptions.impl;

import org.apache.http.HttpStatus;
import vn.edu.iuh.authservice.exceptions.AppException;

/**
 * @description Exception thrown when a user is not found
 * @author: vie
 * @date: 14/2/25
 */
public class UserNotFoundException extends AppException {
    public UserNotFoundException() {
        super(HttpStatus.SC_NOT_FOUND, "User not found");
    }
}
