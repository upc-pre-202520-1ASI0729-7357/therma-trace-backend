package com.thermatrace.thermatracebackend.iam.interfaces.rest.resources;

public record UpdatePaymentMethodResource(
        String cardholderName,
        String cardNumber,
        String expiryMonth,
        String expiryYear,
        String cvv
) {}