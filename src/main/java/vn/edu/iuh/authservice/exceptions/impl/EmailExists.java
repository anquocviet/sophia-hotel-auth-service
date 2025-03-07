package vn.edu.iuh.authservice.exceptions.impl;

import org.apache.http.HttpStatus;
import vn.edu.iuh.authservice.exceptions.AppException;

/**
 * @description
 * @author: vie
 * @date: 27/2/25
 */
public class EmailExists extends AppException {
   public EmailExists() {
      super(HttpStatus.SC_BAD_REQUEST, "Email already exists");
   }
}
