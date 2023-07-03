package com.gymmanagement.security.token.jwt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    @Query("SELECT t" +
            " FROM jwt_token t INNER JOIN User u ON t.user.id = u.id" +
            " WHERE u.id = :id AND (t.isExpired = FALSE OR t.isRevoked = FALSE)")
    List<JwtToken> findAllValidTokenByUser(Long id);
}