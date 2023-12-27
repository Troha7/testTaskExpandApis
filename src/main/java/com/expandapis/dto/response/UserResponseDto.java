package com.expandapis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * {@link UserResponseDto}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserResponseDto {

  private Long id;
  private String username;
  private String password;

}