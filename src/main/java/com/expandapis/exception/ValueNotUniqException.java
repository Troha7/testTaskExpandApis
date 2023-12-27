package com.expandapis.exception;

/**
 * {@link ValueNotUniqException}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */
public class ValueNotUniqException extends RuntimeException {

  public ValueNotUniqException(String message) {
    super(message);
  }
}
