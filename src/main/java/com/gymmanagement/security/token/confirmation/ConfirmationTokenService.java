package com.gymmanagement.security.token.confirmation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;


    public boolean isTokenPresent(String token) {
        return confirmationTokenRepository.findByToken(token).isPresent();
    }

    public boolean hasTokenAvailable(Long id) {
        if (confirmationTokenRepository.getUserLastToken(id) == null) {
            return false;
        }

        return confirmationTokenRepository
                .getUserLastToken(id)
                .getExpiresAt()
                .isBefore(LocalDateTime.now());
    }

    public boolean isLinkValid(String token) {
        if (isTokenPresent(token)) {
            ConfirmationToken confirmationToken = confirmationTokenRepository.loadByToken(token);
            boolean isUserConfirmed = confirmationToken.getUser().getConfirmedAt() == null;

            return !confirmationToken.getExpiresAt().isBefore(LocalDateTime.now()) && isUserConfirmed;
        }

        return false;
    }
}