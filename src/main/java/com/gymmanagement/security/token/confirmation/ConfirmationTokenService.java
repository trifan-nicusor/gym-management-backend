package com.gymmanagement.security.token.confirmation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken findByToken(String token) {
        return confirmationTokenRepository.loadByToken(token);
    }

    public boolean isTokenPresent(String token) {
        return confirmationTokenRepository.findByToken(token).isPresent();
    }

    public boolean hasTokenAvailable(Long id) {
        return confirmationTokenRepository
                .getUserLastToken(id)
                .getExpiresAt()
                .isBefore(LocalDateTime.now());
    }
}