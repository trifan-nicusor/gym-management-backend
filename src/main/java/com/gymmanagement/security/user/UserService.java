package com.gymmanagement.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    public Optional<User> loadByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
