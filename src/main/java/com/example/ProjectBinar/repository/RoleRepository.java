package com.example.ProjectBinar.repository;

import com.example.ProjectBinar.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository untuk entity Role.
 *
 * <p>JpaRepository menyediakan method CRUD standar: - save(), findById(), findAll(), deleteById(),
 * dll.
 *
 * <p>Spring Data JPA akan otomatis mengimplementasikan interface ini berdasarkan nama method (query
 * method).
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  /**
   * Mencari role berdasarkan nama. Spring Data JPA akan otomatis membuat query: SELECT * FROM roles
   * WHERE name = ?
   */
  Optional<Role> findByName(String name);
}
