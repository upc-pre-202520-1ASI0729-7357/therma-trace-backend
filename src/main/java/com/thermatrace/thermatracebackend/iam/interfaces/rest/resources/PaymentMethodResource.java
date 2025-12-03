package com.thermatrace.thermatracebackend.iam.interfaces.rest.resources;

public record PaymentMethodResource(
        Long id,
        String cardholderName,
        String lastFourDigits,
        String maskedCardNumber,  // e.g., "**** **** **** 1234"
        String cardType,
        String expiryMonth,
        String expiryYear,
        String expiry  // e.g., "12/2025"
) {}