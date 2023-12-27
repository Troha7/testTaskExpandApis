package com.expandapis.repository;

import com.expandapis.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * {@link UserRepository}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);

}
