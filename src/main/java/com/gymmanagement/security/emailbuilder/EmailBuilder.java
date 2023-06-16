package com.gymmanagement.security.emailbuilder;

public interface EmailBuilder {
    String confirmationEmailBuilder(String name, String link);
    String forgotPasswordEmailBuilder(String name, String link);
}
