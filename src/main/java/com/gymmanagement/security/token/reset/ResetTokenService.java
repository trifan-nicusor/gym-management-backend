package com.gymmanagement.security.token.reset;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResetTokenService {

    private final ResetTokenRepository resetTokenRepository;

    public Optional<ResetToken> findByToken(String token) {
        return resetTokenRepository.findByToken(token);
    }

    public boolean isTokenPresent(String token) {
        return findByToken(token).isPresent();
    }
}