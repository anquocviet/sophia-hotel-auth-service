package vn.edu.iuh.authservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppException extends RuntimeException {
   private final int code;
   private final String message;
}
