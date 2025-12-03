package com.thermatrace.thermatracebackend.iam.application.internal.outboundservices;

public interface TokenService {
    String generateToken(String email);
    String extractEmail(String token);
    boolean validateToken(String token);
}