package com.thermatrace.thermatracebackend.iam.infrastructure.persistence.jpa;

import com.thermatrace.thermatracebackend.iam.domain.model.entities.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}