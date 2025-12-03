package com.thermatrace.thermatracebackend.iam.interfaces.rest.transform;

import com.thermatrace.thermatracebackend.iam.domain.model.commands.AddPaymentMethodCommand;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.CreatePaymentMethodResource;

public class CreatePaymentMethodCommandFromResourceAssembler {

    public static AddPaymentMethodCommand toCommandFromResource(Long userId, CreatePaymentMethodResource resource) {
        return new AddPaymentMethodCommand(
                userId,
                resource.cardholderName(),
                resource.cardNumber(),
                resource.expiryMonth(),
                resource.expiryYear(),
                resource.cvv()
        );
    }
}