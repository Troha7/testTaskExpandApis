package com.expandapis.service.interfaces;

import com.expandapis.dto.request.UserRequestDto;
import com.expandapis.dto.response.AuthResponseDto;
import com.expandapis.dto.response.UserResponseDto;
import com.expandapis.exception.InvalidCredentialsException;
import com.expandapis.exception.UserNotFoundException;
import com.expandapis.exception.ValueNotUniqException;
import com.expandapis.model.User;

/**
 * {@link UserService} This service handles user registration, authentication,
 * and get user by username.
 *
 * @author Dmytro Trotsenko on 12/24/23
 */
public interface UserService {

  /**
   * Adds a new user to the repository.
   *
   * @param userRequestDto The user details to be added.
   * @return The response containing details of the added user.
   * @throws ValueNotUniqException If the chosen username is already in use.
   */
  UserResponseDto add(UserRequestDto userRequestDto);

  /**
   * Authenticates a user based on the provided credentials.
   *
   * @param userRequestDto The user credentials for authentication.
   * @return The response containing the access token.
   * @throws InvalidCredentialsException throws if invalid credentials.
   */
  AuthResponseDto authenticate(UserRequestDto userRequestDto);

  /**
   * Get user by their username.
   *
   * @param username The username of the user to be retrieved.
   * @return The user details.
   * @throws UserNotFoundException If the username is not found in repository.
   */
  User getByUsername(String username);

}
