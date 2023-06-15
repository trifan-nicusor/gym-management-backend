package com.gymmanagement.security.user;

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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public void changePassword(UserDetails user, String password, String oldPassword) {
        boolean checkIfPasswordMatches = passwordEncoder.matches(oldPassword, user.getPassword());

        if(checkIfPasswordMatches) {
            userRepository.updatePassword(user.getUsername(), passwordEncoder.encode(password));
        }
    }
}
