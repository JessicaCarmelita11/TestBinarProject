package com.example.ProjectBinar.repository;

import com.example.ProjectBinar.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository untuk entity User.
 *
 * <p>JpaRepository menyediakan method CRUD standar secara otomatis. Method custom seperti
 * findByUsername akan di-generate oleh Spring Data JPA.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Mencari user berdasarkan username. Spring Data JPA akan otomatis membuat query: SELECT * FROM
   * users WHERE username = ?
   */
  Optional<User> findByUsername(String username);

  /** Mencari user berdasarkan email. */
  Optional<User> findByEmail(String email);
}
