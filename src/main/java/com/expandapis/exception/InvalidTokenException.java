package com.expandapis.exception;

/**
 * {@link InvalidTokenException}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

public class InvalidTokenException extends RuntimeException {

  public InvalidTokenException(String message) {
    super(message);
  }
}
