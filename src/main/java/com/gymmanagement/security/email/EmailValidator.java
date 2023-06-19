package com.gymmanagement.security.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {

    @Value("${email-regex}")
    private String emailRegex;

    @Override
    public boolean test(String email) {
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
}