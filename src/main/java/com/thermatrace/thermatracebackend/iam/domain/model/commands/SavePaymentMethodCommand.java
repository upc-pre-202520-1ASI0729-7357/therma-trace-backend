package com.thermatrace.thermatracebackend.iam.domain.model.commands;

public record SavePaymentMethodCommand(
    Long userId,
    String cardholderName,
    String cardNumber,  // Will only store last 4 digits
    String cardType,
    String expiryMonth,
    String expiryYear,
    String cvv  // NOT stored - just for validation
) {}