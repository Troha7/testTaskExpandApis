package com.expandapis.service.interfaces;

import com.expandapis.dto.request.ProductRequestDto;
import com.expandapis.dto.response.ProductResponseDto;
import com.expandapis.exception.ValueNotUniqException;
import java.util.List;

/**
 * {@link ProductService} This service handles the addition of new products and get all products.
 *
 * @author Dmytro Trotsenko on 12/25/23
 */
public interface ProductService {

  /**
   * Adds a new product records to the repository.
   *
   * @param productRequestDto The details of the product to be added.
   * @return The response containing details of the added product.
   * @throws ValueNotUniqException If the specified table name is already in use.
   */
  ProductResponseDto add(ProductRequestDto productRequestDto);

  /**
   * Get all product records from the repository.
   *
   * @return The list of tables with records containing details of all products.
   */
  List<ProductResponseDto> all();
}
