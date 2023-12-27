package com.expandapis.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * {@link RecordResponseDto}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RecordResponseDto {

  private Long id;
  private LocalDate entryDate;
  private Integer itemCode;
  private String itemName;
  private Integer itemQuantity;
  private String status;

}