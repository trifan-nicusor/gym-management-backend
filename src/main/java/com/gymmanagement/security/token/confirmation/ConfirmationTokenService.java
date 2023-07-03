package com.gymmanagement.security.token.confirmation;

public interface ConfirmationTokenService {
    boolean isTokenPresent(String token);

    ConfirmationToken getLastUserToken(Long id);

    boolean hasTokenAvailable(Long id);

    boolean isLinkValid(String token);
}