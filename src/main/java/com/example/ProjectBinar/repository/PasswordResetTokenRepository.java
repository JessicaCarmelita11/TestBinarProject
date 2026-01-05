package com.example.ProjectBinar.repository;

import com.example.ProjectBinar.entity.PasswordResetToken;
import com.example.ProjectBinar.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Repository untuk PasswordResetToken entity. */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

  /** Cari token berdasarkan string token. */
  Optional<PasswordResetToken> findByToken(String token);

  /** Cari token berdasarkan user. */
  Optional<PasswordResetToken> findByUser(User user);

  /** Hapus semua token milik user tertentu. */
  void deleteByUser(User user);
}
