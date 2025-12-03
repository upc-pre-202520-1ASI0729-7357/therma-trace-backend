package com.thermatrace.thermatracebackend.iam.interfaces.rest.transform;

import com.thermatrace.thermatracebackend.iam.domain.model.entities.PaymentMethod;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.PaymentMethodResource;

public class PaymentMethodResourceFromEntityAssembler {

    public static PaymentMethodResource toResourceFromEntity(PaymentMethod entity) {
        return new PaymentMethodResource(
                entity.getId(),
                entity.getCardholderName(),
                entity.getLastFourDigits(),
                entity.getMaskedCardNumber(),
                entity.getCardType(),
                entity.getExpiryMonth(),
                entity.getExpiryYear(),
                entity.getExpiry()
        );
    }
}