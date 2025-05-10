package vn.edu.iuh.authservice.exceptions.impl;

import org.apache.http.HttpStatus;
import vn.edu.iuh.authservice.exceptions.AppException;

/**
 * @description Exception thrown when a username already exists
 * @author: vie
 * @date: 14/2/25
 */
public class UsernameExistsException extends AppException {
    public UsernameExistsException() {
        super(HttpStatus.SC_CONFLICT, "Username already exists");
    }
}
