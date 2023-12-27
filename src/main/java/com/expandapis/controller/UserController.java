package com.expandapis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.expandapis.dto.request.UserRequestDto;
import com.expandapis.dto.response.AuthResponseDto;
import com.expandapis.dto.response.UserResponseDto;
import com.expandapis.service.interfaces.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link UserController}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping(path = "/add", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public UserResponseDto add(@Valid @RequestBody UserRequestDto userRequestDto) {
    return userService.add(userRequestDto);
  }

  @PostMapping(path = "/authenticate", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public AuthResponseDto authenticate(@Valid @RequestBody UserRequestDto userRequestDto) {
    return userService.authenticate(userRequestDto);
  }
}
