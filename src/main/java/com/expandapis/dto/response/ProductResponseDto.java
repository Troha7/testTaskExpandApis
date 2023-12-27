package com.expandapis.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * {@link ProductResponseDto}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductResponseDto {

  private String table;
  private List<RecordResponseDto> records;

}