package com.expandapis.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * {@link Product}
 *
 * @author Dmytro Trotsenko on 12/25/23
 */

@NoArgsConstructor
@Getter
@Entity
@Table
@ToString
public class Product {

  @Id
  @Column(nullable = false, unique = true)
  private String tableName;

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Record> records;

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public void setRecords(List<Record> records) {
    this.records = records.stream()
        .peek(r -> r.setProduct(this))
        .toList();
  }
}
