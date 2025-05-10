package vn.edu.iuh.authservice.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.edu.iuh.authservice.dtos.responses.ErrorResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description Global exception handler for all application exceptions
 * @author: vie
 * @date: 14/2/25
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

   @ExceptionHandler(AppException.class)
   public ResponseEntity<ErrorResponse> handleAppException(AppException e, WebRequest request) {
      log.error("Application error: {}", e.getMessage(), e);
      
      ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(e.getCode())
            .error("Application Error")
            .message(e.getMessage())
            .path(getRequestPath(request))
            .build();
            
      return ResponseEntity.status(e.getCode()).body(errorResponse);
   }

   @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
   public ResponseEntity<ErrorResponse> handleValidationException(Exception e, WebRequest request) {
      log.error("Validation error: {}", e.getMessage(), e);
      
      List<String> details = null;
      
      if (e instanceof BindException bindException) {
         details = bindException.getBindingResult().getFieldErrors()
               .stream()
               .map(this::formatFieldError)
               .toList();
      }
      
      ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.SC_BAD_REQUEST)
            .error("Validation Error")
            .message("Input validation failed")
            .path(getRequestPath(request))
            .details(details)
            .build();
            
      return ResponseEntity.status(HttpStatus.SC_BAD_REQUEST).body(errorResponse);
   }

   @ExceptionHandler(NoResourceFoundException.class)
   public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e, WebRequest request) {
      log.error("Resource not found: {}", e.getMessage(), e);
      
      ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.SC_NOT_FOUND)
            .error("Resource Not Found")
            .message(e.getMessage())
            .path(getRequestPath(request))
            .build();
            
      return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(errorResponse);
   }
   
   @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
   public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception e, WebRequest request) {
      log.error("Authentication error: {}", e.getMessage(), e);
      
      ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.SC_UNAUTHORIZED)
            .error("Authentication Error")
            .message(e.getMessage())
            .path(getRequestPath(request))
            .build();
            
      return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).body(errorResponse);
   }
   
   @ExceptionHandler(AccessDeniedException.class)
   public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
      log.error("Access denied: {}", e.getMessage(), e);
      
      ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.SC_FORBIDDEN)
            .error("Access Denied")
            .message("You do not have permission to access this resource")
            .path(getRequestPath(request))
            .build();
            
      return ResponseEntity.status(HttpStatus.SC_FORBIDDEN).body(errorResponse);
   }

   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> handleUnwantedException(Exception e, WebRequest request) {
      log.error("Unexpected error: {}", e.getMessage(), e);
      
      ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            .error("Internal Server Error")
            .message(e.getMessage())
            .path(getRequestPath(request))
            .build();
            
      return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(errorResponse);
   }
   
   /**
    * Extracts the request path from WebRequest
    */
   private String getRequestPath(WebRequest request) {
      if (request instanceof ServletWebRequest) {
         return ((ServletWebRequest) request).getRequest().getRequestURI();
      }
      return "";
   }
   
   /**
    * Formats field errors for validation exceptions
    */
   private String formatFieldError(FieldError fieldError) {
      return fieldError.getField() + ": " + fieldError.getDefaultMessage();
   }
}
