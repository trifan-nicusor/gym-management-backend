package com.gymmanagement.security.token.reset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    Optional<ResetToken> findByToken(String token);

    @Query("SELECT t FROM reset_token t INNER JOIN User u ON t.user = u " +
            "WHERE u.id = ?1 ORDER BY t.createdAt DESC LIMIT 1")
    ResetToken getUserLastToken(Long id);
}