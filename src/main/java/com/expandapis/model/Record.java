package com.expandapis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * {@link Record}
 *
 * @author Dmytro Trotsenko on 12/25/23
 */

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Record {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false)
  private LocalDate entryDate;
  @Column(nullable = false)
  private Integer itemCode;
  @Column(nullable = false)
  private String itemName;
  @Column(nullable = false)
  private Integer itemQuantity;

  @Column(nullable = false, length = 32)
  private String status;
  @ManyToOne
  @JoinColumn(name = "product_table", referencedColumnName = "tableName", nullable = false)
  private Product product;

}
