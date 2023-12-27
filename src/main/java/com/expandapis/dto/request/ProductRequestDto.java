package com.expandapis.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * {@link ProductRequestDto}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductRequestDto {

  @NotEmpty(message = "Table cannot be empty")
  @Size(min = 3, max = 70, message = "Table length should be from 3 to 70 symbols")
  @NotNull(message = "Table should not be null")
  private String table;

  @NotEmpty(message = "Records should has at least 1 record")
  @NotNull(message = "Records should has at least 1 record")
  private List<@Valid RecordRequestDto> records;

}