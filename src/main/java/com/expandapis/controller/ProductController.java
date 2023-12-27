package com.expandapis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.expandapis.dto.request.ProductRequestDto;
import com.expandapis.dto.response.ProductResponseDto;
import com.expandapis.service.interfaces.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link ProductController}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @PostMapping(path = "/add", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ProductResponseDto add(@Valid @RequestBody ProductRequestDto productRequestDto) {
    return productService.add(productRequestDto);
  }

  @GetMapping(path = "/all", produces = APPLICATION_JSON_VALUE)
  public List<ProductResponseDto> all() {
    return productService.all();
  }

}
