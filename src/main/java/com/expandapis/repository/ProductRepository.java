package com.expandapis.repository;

import com.expandapis.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link ProductRepository}
 *
 * @author Dmytro Trotsenko on 12/25/23
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

  boolean existsByTableName(String tableName);
}
