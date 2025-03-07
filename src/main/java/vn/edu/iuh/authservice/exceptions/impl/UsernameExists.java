package vn.edu.iuh.authservice.exceptions.impl;

import org.apache.http.HttpStatus;
import vn.edu.iuh.authservice.exceptions.AppException;

public class UsernameExists extends AppException {
   public UsernameExists() {
      super(HttpStatus.SC_BAD_REQUEST, "Username already exists");
   }
}
