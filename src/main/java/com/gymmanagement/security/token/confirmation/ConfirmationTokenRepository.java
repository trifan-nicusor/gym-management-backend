package com.gymmanagement.security.token.confirmation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    Optional<ConfirmationToken> findByToken(String token);

    @Query("SELECT t FROM confirmation_token t WHERE t.token = ?1")
    ConfirmationToken loadByToken(String token);

    @Query("SELECT t FROM confirmation_token t INNER JOIN User u ON t.user = u " +
            "WHERE u.id = ?1 ORDER BY t.createdAt DESC LIMIT 1")
    ConfirmationToken getUserLastToken(Long id);
}