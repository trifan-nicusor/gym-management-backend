package com.gymmanagement.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("SELECT t FROM Token t INNER JOIN User u ON t.user.id = u.id" +
            " WHERE u.id = :id AND (t.isExpired = false OR t.isExpired = false )")
    List<Token> findAllValidTokenByUser(Long id);
}