package vn.edu.iuh.authservice.exceptions.impl;

import org.apache.http.HttpStatus;
import vn.edu.iuh.authservice.exceptions.AppException;

/**
 * @description
 * @author: vie
 * @date: 27/2/25
 */
public class FailCreateUser extends AppException {
   public FailCreateUser() {
      super(
            HttpStatus.SC_BAD_REQUEST,
            "Fail to create user"
      );
   }
}
