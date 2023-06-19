package com.gymmanagement.security.token.reset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResetTokenService {

    private final ResetTokenRepository resetTokenRepository;

    public Optional<ResetToken> findByToken(String token) {
        return resetTokenRepository.findByToken(token);
    }

    public boolean hasTokenAvailable(Long id) {
        if (resetTokenRepository.getUserLastToken(id) == null) {
            return false;
        }

        return !resetTokenRepository
                .getUserLastToken(id)
                .getExpiresAt()
                .isBefore(LocalDateTime.now());
    }

    public boolean isTokenPresent(String token) {
        return findByToken(token).isPresent();
    }
}