package com.thermatrace.thermatracebackend.iam.infrastructure.email;

import com.thermatrace.thermatracebackend.iam.application.internal.outboundservices.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Mock Email Service - Logs emails instead of sending them
 */
@Service
public class MockEmailService implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void sendPasswordResetEmail(String to, String resetToken) {
        LOGGER.info("MOCK EMAIL: Sending password reset email to {} with token: {}", to, resetToken);
        LOGGER.info("Reset link: http://localhost:4200/reset-password?token={}", resetToken);
    }

    @Override
    public void sendWelcomeEmail(String to, String firstName) {
        LOGGER.info("MOCK EMAIL: Sending welcome email to {}", to);
        LOGGER.info("Welcome {}! Your account has been created successfully.", firstName);
    }
}