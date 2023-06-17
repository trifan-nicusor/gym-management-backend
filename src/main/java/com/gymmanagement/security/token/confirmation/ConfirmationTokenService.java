package com.gymmanagement.security.token.confirmation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public Optional<ConfirmationToken> findByToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public boolean isTokenPresent(String token) {
        return findByToken(token).isPresent();
    }

    public boolean hasTokenAvailable(Long id) {
        return confirmationTokenRepository
                .getUserLastToken(id)
                .getExpiresAt()
                .isBefore(LocalDateTime.now());
    }
}