package com.expandapis.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * {@link RecordRequestDto}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecordRequestDto {

  @Pattern(regexp = "\\d{2}-\\d{2}-\\d{4}$", message = "Entry date mast be [dd-mm-yyyy]")
  @NotNull(message = "Entry date should not be null")
  private String entryDate;

  @Min(value = 1, message = "Item code should be greater or equals 1")
  @NotNull(message = "Item code should not be null")
  private Integer itemCode;

  @NotEmpty(message = "Item name cannot be empty")
  @Size(min = 3, max = 70, message = "Item name length should be from 3 to 70 symbols")
  @NotNull(message = "Item name should not be null")
  private String itemName;

  @Min(value = 1, message = "Item quantity should be greater or equals 1")
  @NotNull(message = "Item quantity should not be null")
  private Integer itemQuantity;

  @NotEmpty(message = "Status cannot be empty")
  @Size(min = 3, max = 30, message = "Status length should be from 3 to 30 symbols")
  @NotNull(message = "Status should not be null")
  private String status;

}