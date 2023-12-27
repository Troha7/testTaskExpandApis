package com.expandapis.exception;

/**
 * {@link UserNotFoundException}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }
}
