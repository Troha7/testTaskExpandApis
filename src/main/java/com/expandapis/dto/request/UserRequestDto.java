package com.expandapis.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * {@link UserRequestDto}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserRequestDto {

  @NotEmpty(message = "Username cannot be empty")
  @Size(min = 3, max = 30, message = "Username length should be from 3 to 30 symbols")
  @NotNull(message = "Username should not be null")
  private String username;

  @NotEmpty(message = "Password cannot be empty")
  @Size(min = 5, max = 2048, message = "Password length should be from 5 to 2048 symbols")
  @NotNull(message = "Password should not be null")
  private String password;

}