package vn.edu.iuh.authservice.exceptions.impl;
import org.apache.http.HttpStatus;
import vn.edu.iuh.authservice.exceptions.AppException;

/**
 * @description Exception thrown when a phone number already exists in the system
 * @author: vie
 * @date: 14/2/25
 */
public class PhoneExistsException extends AppException {
    public PhoneExistsException() {
        super(HttpStatus.SC_CONFLICT, "Phone number already exists");
    }
}
