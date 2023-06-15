package com.gymmanagement.security.user;

import com.gymmanagement.security.email.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailValidator emailValidator;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public void changePassword(UserDetails user, String password) {
        userRepository.updatePassword(user.getUsername(), passwordEncoder.encode(password));
    }

    public boolean checkIfPasswordMatches(UserDetails user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    public boolean userExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isEmailValid(String email) {
        return emailValidator.test(email);
    }

    public void enableUser(String email) {
        userRepository.enableUser(email);
    }
}
