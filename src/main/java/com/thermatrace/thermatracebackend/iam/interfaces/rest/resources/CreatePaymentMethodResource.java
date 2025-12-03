package com.thermatrace.thermatracebackend.iam.interfaces.rest.resources;

public record CreatePaymentMethodResource(
        String cardholderName,
        String cardNumber,
        String expiryMonth,
        String expiryYear,
        String cvv
) {}