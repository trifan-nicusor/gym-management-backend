package com.gymmanagement.security.email;

public interface EmailSender {
    void send(String to, String email);
}