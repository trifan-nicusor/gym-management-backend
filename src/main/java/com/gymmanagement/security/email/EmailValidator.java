package com.gymmanagement.security.email;

import org.springframework.stereotype.Service;
import java.util.function.Predicate;

@Service
public class EmailValidator implements Predicate<String> {

    @Override
    public boolean test(String email) {
        return true;
    }
}