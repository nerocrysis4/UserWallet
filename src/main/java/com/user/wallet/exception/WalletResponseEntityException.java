package com.user.wallet.exception;

import com.user.wallet.model.exception.ResponseExceptionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class WalletResponseEntityException extends ResponseEntityExceptionHandler {

  @ExceptionHandler(WalletException.class)
  public final ResponseEntity<Object> handleExceptions(WalletException ex) {

    ResponseExceptionModel exceptionModel = new ResponseExceptionModel();
    exceptionModel.setMessage(ex.getMessage());
    HttpStatus responseStatus = ex.getHttpStatus();

    return new ResponseEntity(exceptionModel, responseStatus);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public final ResponseEntity<Object> handleAccessDeniedException(HttpServletResponse response) throws IOException {
    log.error("Unauthorized User");
    return new ResponseEntity("Unauthorized User", HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<Object> handleException(HttpServletResponse response) throws IOException {
    log.error("Something went wrong");
    return new ResponseEntity("Something went wrong", HttpStatus.BAD_REQUEST);
  }

}
