package vn.edu.iuh.authservice.exceptions.impl;

import org.apache.http.HttpStatus;
import vn.edu.iuh.authservice.exceptions.AppException;

/**
 * @description Exception thrown when an email already exists
 * @author: vie
 * @date: 14/2/25
 */
public class EmailExistsException extends AppException {
    public EmailExistsException() {
        super(HttpStatus.SC_CONFLICT, "Email already exists");
    }
}
