package com.gymmanagement.security.user;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u" +
            " FROM User u" +
            " WHERE u.email = ?1")
    User loadByEmail(String email);

    @Query("SELECT u.isEnabled" +
            " FROM User u" +
            " WHERE u.email = ?1")
    boolean isUserEnabled(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u" +
            " SET u.isEnabled = TRUE , u.confirmedAt = CURRENT_TIMESTAMP, u.updatedAt = CURRENT_TIMESTAMP" +
            " WHERE u.email = ?1")
    void enableUser(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u" +
            " SET u.password = ?2, u.updatedAt = CURRENT_TIMESTAMP" +
            " WHERE u.email = ?1")
    void updatePassword(String email, String password);
}