package com.gymmanagement.security.token.reset;

import java.util.Optional;

public interface ResetTokenService {
    Optional<ResetToken> findByToken(String token);

    ResetToken getUserLastToken(Long id);

    boolean hasTokenAvailable(Long id);

    boolean isTokenPresent(String token);
}