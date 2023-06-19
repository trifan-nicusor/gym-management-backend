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

    public ResetToken getUserLastToken(Long id) {
        return resetTokenRepository.getUserLastToken(id);
    }

    public boolean hasTokenAvailable(Long id) {
        if (getUserLastToken(id) == null) {
            return false;
        }

        return !getUserLastToken(id)
                .getExpiresAt()
                .isBefore(LocalDateTime.now());
    }

    public boolean isTokenPresent(String token) {
        return findByToken(token).isPresent();
    }
}