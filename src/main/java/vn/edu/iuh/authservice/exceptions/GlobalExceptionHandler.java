package vn.edu.iuh.authservice.exceptions;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.edu.iuh.authservice.dtos.responses.ExceptionResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @description
 * @author: vie
 * @date: 14/2/25
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
   private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class.getName());

   @ExceptionHandler(AppException.class)
   public ResponseEntity<ExceptionResponse> handleAppException(AppException e) {
      logger.log(Level.SEVERE, "App error: ", e);
      return ResponseEntity.status(e.getCode())
            .body(new ExceptionResponse(e.getCode(), e.getMessage()));
   }

   @ExceptionHandler(BindException.class)
   public ResponseEntity<ExceptionResponse> handleBindException(BindException e) {
      logger.log(Level.SEVERE, "Validation error: ", e);
      return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST)
            .body(new ExceptionResponse(HttpStatus.SC_BAD_REQUEST, e.getMessage()));
   }

   @ExceptionHandler(NoResourceFoundException.class)
   public ResponseEntity<ExceptionResponse> handleNoResourceFoundException(NoResourceFoundException e) {
      logger.log(Level.SEVERE, "Resource not found: ", e);
      return ResponseEntity.status(HttpStatus.SC_NOT_FOUND)
            .body(new ExceptionResponse(HttpStatus.SC_NOT_FOUND, e.getMessage()));
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<ExceptionResponse> handleUnwantedException(Exception e) {
      logger.log(Level.SEVERE, "Unknown error: ", e);
      return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            .body(new ExceptionResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage()));
   }
}
