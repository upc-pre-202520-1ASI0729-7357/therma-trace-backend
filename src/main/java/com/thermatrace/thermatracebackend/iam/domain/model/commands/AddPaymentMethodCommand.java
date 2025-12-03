package com.thermatrace.thermatracebackend.iam.domain.model.commands;

public record AddPaymentMethodCommand(
        Long userId,
        String cardholderName,
        String cardNumber,  // Full card number - backend will extract last 4 digits and detect type
        String expiryMonth,
        String expiryYear,
        String cvv  // For validation only, never stored
) {}