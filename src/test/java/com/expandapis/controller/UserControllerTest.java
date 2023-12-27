package com.expandapis.controller;

import static com.github.springtestdbunit.annotation.DatabaseOperation.CLEAN_INSERT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.expandapis.config.MySqlTestContainerConfig;
import com.expandapis.exception.InvalidCredentialsException;
import com.expandapis.exception.ValueNotUniqException;
import com.expandapis.repository.UserRepository;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

/**
 * {@link UserControllerTest}
 *
 * @author Dmytro Trotsenko on 12/27/23
 */

@ExtendWith(MySqlTestContainerConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
@DatabaseSetup(value = "/dataset/user.xml", type = CLEAN_INSERT)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private UserRepository userRepository;

  private static final String URI = "/user";

  // ----------------------------------
  //               POST
  // ----------------------------------

  @Test
  @DisplayName("add should add a new user")
  public void add_shouldAddNewUser() throws Exception {
    // Given
    String newUser = """
        {
            "username": "taras",
            "password" : "password-123"
        }
        """;

    // When
    var result = mockMvc
        .perform(post(URI + "/add")
            .content(newUser)
            .contentType(APPLICATION_JSON));

    // Then
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.username").value("taras"))
        .andExpect(jsonPath("$.password").value("********"));

    assertThat(userRepository.findByUsername("taras")).isPresent();
  }

  @Test
  @DisplayName("add should return 409 if username not uniq")
  public void add_shouldReturn409IfUsernameNotUniq() throws Exception {
    // Given
    String newUser = """
        {
            "username": "vasil",
            "password" : "password-123"
        }
        """;

    // When
    var result = mockMvc
        .perform(post(URI + "/add")
            .content(newUser)
            .contentType(APPLICATION_JSON));

    // Then
    result
        .andExpect(status().isConflict())
        .andExpect(res ->
            assertThat(res.getResolvedException()).isInstanceOf(ValueNotUniqException.class));
  }

  @Test
  @DisplayName("authenticate should return access token")
  public void authenticate_shouldReturnAccessToken() throws Exception {
    // Given
    String user = """
        {
            "username": "vasil",
            "password" : "password-123"
        }
        """;

    // When
    var result = mockMvc
        .perform(post(URI + "/authenticate")
            .content(user)
            .contentType(APPLICATION_JSON));

    // Then
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.accessToken").exists())
        .andExpect(jsonPath("$.accessToken").isString());
  }

  @Test
  @DisplayName("authenticate should return 401 if invalid username")
  public void authenticate_shouldReturn401IfInvalidUsername() throws Exception {
    // Given
    String user = """
        {
            "username": "not_exist_user",
            "password" : "password-123"
        }
        """;

    // When
    var result = mockMvc
        .perform(post(URI + "/authenticate")
            .content(user)
            .contentType(APPLICATION_JSON));

    // Then
    result
        .andExpect(status().isUnauthorized())
        .andExpect(res ->
            assertThat(res.getResolvedException()).isInstanceOf(InvalidCredentialsException.class));
  }

  @Test
  @DisplayName("authenticate should return 401 if invalid password")
  public void authenticate_shouldReturn401IfInvalidPassword() throws Exception {
    // Given
    String user = """
        {
            "username": "vasil",
            "password" : "invalid_password"
        }
        """;

    // When
    var result = mockMvc
        .perform(post(URI + "/authenticate")
            .content(user)
            .contentType(APPLICATION_JSON));

    // Then
    result
        .andExpect(status().isUnauthorized())
        .andExpect(res ->
            assertThat(res.getResolvedException()).isInstanceOf(InvalidCredentialsException.class));
  }
}
