package com.expandapis.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * {@link ApiErrorResponseDto}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@Getter
@RequiredArgsConstructor
public class ApiErrorResponseDto <T> {

  private final HttpStatus httpStatus;
  private final T message;

}
