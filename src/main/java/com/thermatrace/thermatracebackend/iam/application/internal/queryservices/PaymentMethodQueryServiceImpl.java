package com.thermatrace.thermatracebackend.iam.application.internal.queryservices;

import com.thermatrace.thermatracebackend.iam.domain.model.entities.PaymentMethod;
import com.thermatrace.thermatracebackend.iam.domain.model.queries.GetPaymentMethodByUserIdQuery;
import com.thermatrace.thermatracebackend.iam.domain.services.PaymentMethodQueryService;
import com.thermatrace.thermatracebackend.iam.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentMethodQueryServiceImpl implements PaymentMethodQueryService {

    private final UserRepository userRepository;

    public PaymentMethodQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<PaymentMethod> handle(GetPaymentMethodByUserIdQuery query) {
        return userRepository.findById(query.userId())
                .map(user -> user.getPaymentMethod());
    }
}