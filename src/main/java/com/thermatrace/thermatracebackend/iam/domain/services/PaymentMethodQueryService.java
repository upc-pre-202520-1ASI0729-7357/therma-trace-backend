package com.thermatrace.thermatracebackend.iam.domain.services;

import com.thermatrace.thermatracebackend.iam.domain.model.entities.PaymentMethod;
import com.thermatrace.thermatracebackend.iam.domain.model.queries.GetPaymentMethodByUserIdQuery;

import java.util.Optional;

public interface PaymentMethodQueryService {
    Optional<PaymentMethod> handle(GetPaymentMethodByUserIdQuery query);
}