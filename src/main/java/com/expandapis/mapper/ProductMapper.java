package com.expandapis.mapper;

import com.expandapis.dto.request.ProductRequestDto;
import com.expandapis.dto.request.RecordRequestDto;
import com.expandapis.dto.response.ProductResponseDto;
import com.expandapis.model.Product;
import com.expandapis.model.Record;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * {@link ProductMapper}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Mapping(target = "table", source = "tableName")
  ProductResponseDto toResponseDto(Product product);

  @Mapping(target = "table", source = "tableName")
  List<ProductResponseDto> toListResponseDto(List<Product> product);

  @Mapping(target = "tableName", source = "table")
  @Mapping(target = "records", expression = "java(toListRecord(productRequestDto.getRecords()))")
  Product toEntity(ProductRequestDto productRequestDto);

  @Mapping(target = "entryDate", expression = "java(toLocalDate(recordRequestDtos.entryDate))")
  List<Record> toListRecord(List<RecordRequestDto> recordRequestDtos);

  default LocalDate toLocalDate(String entryDate) {
    return LocalDate.parse(entryDate, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
  }

}
