package com.expandapis.service;

import com.expandapis.dto.request.ProductRequestDto;
import com.expandapis.dto.response.ProductResponseDto;
import com.expandapis.exception.ValueNotUniqException;
import com.expandapis.mapper.ProductMapper;
import com.expandapis.repository.ProductRepository;
import com.expandapis.service.interfaces.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link ProductServiceImpl}
 *
 * @author Dmytro Trotsenko on 12/25/23
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Transactional
  @Override
  public ProductResponseDto add(ProductRequestDto productRequestDto) {
    log.trace("Started add product records to repository");

    checkingUniqTableName(productRequestDto.getTable());
    var product = productRepository.save(productMapper.toEntity(productRequestDto));

    log.info("Product records added successfully");
    return productMapper.toResponseDto(product);
  }

  @Transactional(readOnly = true)
  @Override
  public List<ProductResponseDto> all() {
    log.trace("Started find all product records to repository");

    var products = productRepository.findAll();

    log.info("All product records found successfully");
    return productMapper.toListResponseDto(products);
  }

  private void checkingUniqTableName(String tableName) {
    if (productRepository.existsByTableName(tableName)) {
      log.warn("Table name [{}] already in use", tableName);
      throw new ValueNotUniqException(String.format("Table name %s already in use", tableName));
    }
  }
}
