package com.expandapis.controller;

import static com.github.springtestdbunit.annotation.DatabaseOperation.CLEAN_INSERT;
import static com.github.springtestdbunit.annotation.DatabaseOperation.DELETE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.expandapis.config.MySqlTestContainerConfig;
import com.expandapis.exception.ValueNotUniqException;
import com.expandapis.model.User;
import com.expandapis.repository.ProductRepository;
import com.expandapis.security.JwtService;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.BeforeEach;
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
 * {@link ProductControllerTest}
 *
 * @author Dmytro Trotsenko on 12/26/23
 */

@ExtendWith(MySqlTestContainerConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
@DatabaseSetup(value = {
    "/dataset/product.xml",
    "/dataset/record.xml",
    "/dataset/user.xml"},
    type = CLEAN_INSERT)
public class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private JwtService jwtService;
  @Autowired
  private ProductRepository productRepository;

  private static final String URI = "/products";
  private static final String TOKEN_TYPE = "Bearer ";
  private static final String USERNAME = "anton";
  private static final String PASSWORD = "password-123";

  private static String accessToken;

  @BeforeEach
  public void setup() {
    accessToken = getAccessToken();
  }

  // ----------------------------------
  //               POST
  // ----------------------------------

  @Test
  @DisplayName("add should add a new product records")
  public void add_shouldAddNewProductRecords() throws Exception {
    // Given
    String newProducts = """
        {
        "table" : "new products",
        "records" : [
        {
        "entryDate": "03-01-2023",
        "itemCode": "11111",
        "itemName": "Test Inventory 1",
        "itemQuantity": "20",
        "status": "Paid"
        },
        {
        "entryDate": "03-01-2023",
        "itemCode": "11111",
        "itemName": "Test Inventory 2",
        "itemQuantity": "20",
        "status": "Paid"
        }] }
        """;

    // When
    var result = mockMvc
        .perform(post(URI + "/add")
            .content(newProducts)
            .with(request -> {
              request.addHeader(AUTHORIZATION, TOKEN_TYPE + accessToken);
              return request;
            })
            .contentType(APPLICATION_JSON));

    // Then
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(jsonPath("$.table").value("new products"))
        .andExpect(jsonPath("$.records").isArray())
        .andExpect(jsonPath("$.records[0].entryDate").value("2023-01-03"))
        .andExpect(jsonPath("$.records[0].itemCode").value(11111))
        .andExpect(jsonPath("$.records[0].itemName").value("Test Inventory 1"))
        .andExpect(jsonPath("$.records[0].itemQuantity").value(20))
        .andExpect(jsonPath("$.records[0].status").value("Paid"))
        .andExpect(jsonPath("$.records[1].entryDate").value("2023-01-03"))
        .andExpect(jsonPath("$.records[1].itemCode").value(11111))
        .andExpect(jsonPath("$.records[1].itemName").value("Test Inventory 2"))
        .andExpect(jsonPath("$.records[1].itemQuantity").value(20))
        .andExpect(jsonPath("$.records[1].status").value("Paid"));

    assertThat(productRepository.findById("new products")).isPresent();
  }

  @Test
  @DisplayName("add should return 409 if table name not uniq")
  public void add_shouldReturn409IfTableNameNotUniq() throws Exception {
    // Given
    String newProducts = """
        {
        "table" : "products",
        "records" : [
        {
        "entryDate": "03-01-2023",
        "itemCode": "11111",
        "itemName": "Test Inventory 1",
        "itemQuantity": "20",
        "status": "Paid"
        },
        {
        "entryDate": "03-01-2023",
        "itemCode": "11111",
        "itemName": "Test Inventory 2",
        "itemQuantity": "20",
        "status": "Paid"
        }] }
        """;

    // When
    var result = mockMvc
        .perform(post(URI + "/add")
            .content(newProducts)
            .with(request -> {
              request.addHeader(AUTHORIZATION, TOKEN_TYPE + accessToken);
              return request;
            })
            .contentType(APPLICATION_JSON));

    // Then
    result
        .andExpect(status().isConflict())
        .andExpect(res ->
            assertThat(res.getResolvedException()).isInstanceOf(ValueNotUniqException.class));
  }

  //-----------------------------------
  //               GET
  //-----------------------------------

  @Test
  @DisplayName("all should return empty list of all product records")
  @DatabaseSetup(value = {"/dataset/product.xml", "/dataset/record.xml"}, type = DELETE)
  void findAll_shouldReturnEmptyListOfAllProductRecords() throws Exception {
    // When
    var result = mockMvc
        .perform(get(URI + "/all")
            .with(request -> {
              request.addHeader(AUTHORIZATION, TOKEN_TYPE + accessToken);
              return request;
            }));

    // Then
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().json("[]"));
  }

  @Test
  @DisplayName("all should return all product records")
  void findAll_shouldReturnAllProductRecords() throws Exception {
    // Given
    String expectedJson = """
        [
            {
                "table": "products",
                "records": [
                    {
                        "id": 1,
                        "entryDate": "2023-12-26",
                        "itemCode": 12345,
                        "itemName": "Test Inventory 1",
                        "itemQuantity": 20,
                        "status": "Paid"
                    },
                    {
                        "id": 2,
                        "entryDate": "2023-12-26",
                        "itemCode": 12345,
                        "itemName": "Test Inventory 2",
                        "itemQuantity": 2,
                        "status": "Paid"
                    }
                ]
            },
            {
                "table": "fruits",
                "records": [
                    {
                        "id": 3,
                        "entryDate": "2023-12-26",
                        "itemCode": 54321,
                        "itemName": "Test Inventory 3",
                        "itemQuantity": 37,
                        "status": "Paid"
                    }
                ]
            }
        ]
        """;

    // When
    var result = mockMvc
        .perform(get(URI + "/all")
            .with(request -> {
              request.addHeader(AUTHORIZATION, TOKEN_TYPE + accessToken);
              return request;
            }));

    // Then
    result
        .andExpect(status().isOk())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().json(expectedJson));
  }

  @Test
  @DisplayName("all should return 403 when not set access token")
  void findAll_shouldReturn403WhenNotSetAccessToken() throws Exception {
    // When
    var result = mockMvc
        .perform(get(URI + "/all"));

    // Then
    result
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("all should return 401 when invalid token type")
  void findAll_shouldReturn401WhenInvalidTokenType() throws Exception {
    //Given
    String invalidTokenType = "Invalid_Token_Type";

    // When
    var result = mockMvc
        .perform(get(URI + "/all")
            .with(request -> {
              request.addHeader(AUTHORIZATION, invalidTokenType + accessToken);
              return request;
            }));

    // Then
    result
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().json("""
            {
                "httpStatus": "UNAUTHORIZED",
                "message": "Invalid token type, token type should be [Bearer]"
            }
            """));
  }

  @Test
  @DisplayName("all should return 401 when invalid token")
  void findAll_shouldReturn401WhenInvalidToken() throws Exception {
    //Given
    String invalidToken = "Invalid_Token";

    // When
    var result = mockMvc
        .perform(get(URI + "/all")
            .with(request -> {
              request.addHeader(AUTHORIZATION, TOKEN_TYPE + invalidToken);
              return request;
            }));

    // Then
    result
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andExpect(content().json("""
            {
                  "httpStatus": "UNAUTHORIZED",
                  "message": "JWT strings must contain exactly 2 period characters. Found: 0"
            }
            """));
  }

  @Test
  @DisplayName("all should return 401 when expired access token")
  void findAll_shouldReturn401WhenExpiredAccessToken() throws Exception {
    //Given
    String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2YXNpbCIsImlhdCI6MTcwMzY3MjgzMiwiZXhwIjoxNzAzNjc2NDMyfQ.mZ8lHpKK4WMAtF1HyrE5NvfFPPEYN0hSX497qXyjMnM";

    // When
    var result = mockMvc
        .perform(get(URI + "/all")
            .with(request -> {
              request.addHeader(AUTHORIZATION, TOKEN_TYPE + expiredToken);
              return request;
            }));

    // Then
    result
        .andExpect(status().isUnauthorized())
        .andExpect(content().contentType(APPLICATION_JSON));
  }

  private String getAccessToken() {
    var user = new User();
    user.setUsername(ProductControllerTest.USERNAME);
    user.setPassword(ProductControllerTest.PASSWORD);
    return jwtService.generateAccessToken(user);
  }
}
