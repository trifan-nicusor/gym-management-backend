package com.gymmanagement.security.email.builder;

public interface EmailBuilder {
    String confirmationEmailBuilder(String name, String link);

    String forgotPasswordEmailBuilder(String name, String link);
}