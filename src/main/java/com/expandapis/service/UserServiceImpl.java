package com.expandapis.service;

import com.expandapis.dto.request.UserRequestDto;
import com.expandapis.dto.response.AuthResponseDto;
import com.expandapis.dto.response.UserResponseDto;
import com.expandapis.exception.InvalidCredentialsException;
import com.expandapis.exception.UserNotFoundException;
import com.expandapis.exception.ValueNotUniqException;
import com.expandapis.mapper.UserMapper;
import com.expandapis.model.User;
import com.expandapis.repository.UserRepository;
import com.expandapis.security.JwtService;
import com.expandapis.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link UserServiceImpl}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authManager;
  private final UserMapper userMapper;

  @Transactional
  @Override
  public UserResponseDto add(UserRequestDto userRequestDto) {
    var username = userRequestDto.getUsername();
    log.trace("Started add new user [{}]", username);

    checkingUniqUsername(username);

    var user = userMapper.toEntity(userRequestDto);
    user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
    User savedUser = userRepository.save(user);

    log.info("New user [{}] added to repository", username);
    return userMapper.toResponseDto(savedUser);
  }

  @Transactional
  @Override
  public AuthResponseDto authenticate(UserRequestDto userRequestDto) {
    var username = userRequestDto.getUsername();
    log.trace("Started authenticate user [{}]", username);

    checkingAuthenticateCredentials(userRequestDto);

    User user = getByUsername(username);
    var accessToken = jwtService.generateAccessToken(user);

    log.info("User [{}] authenticated successfully", username);
    return new AuthResponseDto(accessToken);
  }

  @Transactional(readOnly = true)
  @Override
  public User getByUsername(String username) {
    log.trace("Started getByUsername [{}]", username);

    return userRepository.findByUsername(username)
        .orElseThrow(() -> {
          log.warn("User with username [{}] wasn't found in repository", username);
          return new UserNotFoundException(
              String.format("User with username [%s] wasn't found in repository", username));
        });
  }

  private void checkingUniqUsername(String username) {
    if (userRepository.existsByUsername(username)) {
      log.warn("Username [{}] already in use", username);
      throw new ValueNotUniqException(String.format("Username %s already in use", username));
    }
  }

  private void checkingAuthenticateCredentials(UserRequestDto userRequestDto) {
    var username = userRequestDto.getUsername();
    var password = userRequestDto.getPassword();
    try {
      authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (AuthenticationException e) {
      throw new InvalidCredentialsException("Authentication failed, invalid username or password");
    }
  }
}
