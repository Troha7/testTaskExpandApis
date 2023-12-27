package com.expandapis.exception.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.expandapis.dto.response.ApiErrorResponseDto;
import com.expandapis.exception.InvalidCredentialsException;
import com.expandapis.exception.InvalidTokenException;
import com.expandapis.exception.UserNotFoundException;
import com.expandapis.exception.ValueNotUniqException;
import io.jsonwebtoken.JwtException;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * {@link ApiExceptionHandler}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(UserNotFoundException.class)
  public ApiErrorResponseDto<String> handleNotFoundException(RuntimeException exception) {
    return new ApiErrorResponseDto<>(NOT_FOUND, exception.getMessage());
  }

  @ResponseStatus(UNAUTHORIZED)
  @ExceptionHandler({
      InvalidCredentialsException.class,
      InvalidTokenException.class,
      JwtException.class
  })
  public ApiErrorResponseDto<String> handleUnauthorizedException(RuntimeException exception) {
    return new ApiErrorResponseDto<>(UNAUTHORIZED, exception.getMessage());
  }

  @ResponseStatus(CONFLICT)
  @ExceptionHandler(ValueNotUniqException.class)
  public ApiErrorResponseDto<String> handleConflictException(RuntimeException exception) {
    return new ApiErrorResponseDto<>(CONFLICT, exception.getMessage());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      @NonNull HttpHeaders headers,
      @NonNull HttpStatusCode status,
      @NonNull WebRequest request) {
    List<FieldError> fieldErrors = Objects.requireNonNull(ex.getBindingResult()).getFieldErrors();
    List<String> errorMessages = fieldErrors.stream()
        .map(err -> String.format("Invalid '%s': %s", err.getField(), err.getDefaultMessage()))
        .toList();
    return new ResponseEntity<>(new ApiErrorResponseDto<>(BAD_REQUEST, errorMessages), status);
  }
}

