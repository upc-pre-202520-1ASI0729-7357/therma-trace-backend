package com.thermatrace.thermatracebackend.iam.interfaces.rest.transform;

import com.thermatrace.thermatracebackend.iam.domain.model.commands.UpdatePaymentMethodCommand;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.UpdatePaymentMethodResource;

public class UpdatePaymentMethodCommandFromResourceAssembler {

    public static UpdatePaymentMethodCommand toCommandFromResource(Long userId, UpdatePaymentMethodResource resource) {
        return new UpdatePaymentMethodCommand(
                userId,
                resource.cardholderName(),
                resource.cardNumber(),
                resource.expiryMonth(),
                resource.expiryYear(),
                resource.cvv()
        );
    }
}