package com.thermatrace.thermatracebackend.iam.application.internal.outboundservices;

public interface EmailService {
    void sendPasswordResetEmail(String to, String resetToken);
    void sendWelcomeEmail(String to, String firstName);
}