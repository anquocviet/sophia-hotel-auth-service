package vn.edu.iuh.authservice.exceptions.impl;

import org.apache.http.HttpStatus;
import vn.edu.iuh.authservice.exceptions.AppException;

public class UserNotFound extends AppException {
   public UserNotFound() {
      super(HttpStatus.SC_NOT_FOUND, "User not found");
   }
}
