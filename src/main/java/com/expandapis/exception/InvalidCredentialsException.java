package com.expandapis.exception;

/**
 * {@link InvalidCredentialsException}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */
public class InvalidCredentialsException extends RuntimeException {

  public InvalidCredentialsException(String message) {
    super(message);
  }
}
