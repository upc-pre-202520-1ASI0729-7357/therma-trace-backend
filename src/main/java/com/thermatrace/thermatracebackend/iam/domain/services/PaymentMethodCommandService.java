package com.thermatrace.thermatracebackend.iam.domain.services;

import com.thermatrace.thermatracebackend.iam.domain.model.commands.AddPaymentMethodCommand;
import com.thermatrace.thermatracebackend.iam.domain.model.commands.DeletePaymentMethodCommand;
import com.thermatrace.thermatracebackend.iam.domain.model.commands.UpdatePaymentMethodCommand;
import com.thermatrace.thermatracebackend.iam.domain.model.entities.PaymentMethod;

import java.util.Optional;

public interface PaymentMethodCommandService {
    Optional<PaymentMethod> handle(AddPaymentMethodCommand command);
    Optional<PaymentMethod> handle(UpdatePaymentMethodCommand command);
    void handle(DeletePaymentMethodCommand command);
}