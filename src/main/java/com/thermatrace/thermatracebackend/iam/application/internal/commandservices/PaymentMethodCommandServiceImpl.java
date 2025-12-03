package com.thermatrace.thermatracebackend.iam.application.internal.commandservices;

import com.thermatrace.thermatracebackend.iam.domain.model.commands.AddPaymentMethodCommand;
import com.thermatrace.thermatracebackend.iam.domain.model.commands.DeletePaymentMethodCommand;
import com.thermatrace.thermatracebackend.iam.domain.model.commands.UpdatePaymentMethodCommand;
import com.thermatrace.thermatracebackend.iam.domain.model.entities.PaymentMethod;
import com.thermatrace.thermatracebackend.iam.domain.services.CardTypeDetector;
import com.thermatrace.thermatracebackend.iam.domain.services.PaymentMethodCommandService;
import com.thermatrace.thermatracebackend.iam.infrastructure.persistence.jpa.PaymentMethodRepository;
import com.thermatrace.thermatracebackend.iam.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PaymentMethodCommandServiceImpl implements PaymentMethodCommandService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;

    public PaymentMethodCommandServiceImpl(PaymentMethodRepository paymentMethodRepository,
                                           UserRepository userRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Optional<PaymentMethod> handle(AddPaymentMethodCommand command) {
        // Validate card number
        if (!CardTypeDetector.isValidCardNumber(command.cardNumber())) {
            throw new IllegalArgumentException("Invalid card number");
        }

        // Detect card type
        String cardType = CardTypeDetector.detectCardType(command.cardNumber());

        // Validate CVV
        if (!CardTypeDetector.isValidCVV(command.cvv(), cardType)) {
            throw new IllegalArgumentException("Invalid CVV");
        }

        // Validate expiry
        if (!CardTypeDetector.isValidExpiry(command.expiryMonth(), command.expiryYear())) {
            throw new IllegalArgumentException("Invalid or expired card");
        }

        // Get user
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user already has a payment method
        if (user.getPaymentMethod() != null) {
            throw new IllegalStateException("User already has a payment method. Use update instead.");
        }

        // Create payment method (only store last 4 digits)
        var paymentMethod = PaymentMethod.fromCardNumber(
                command.cardholderName(),
                command.cardNumber(),
                cardType,
                command.expiryMonth(),
                command.expiryYear()
        );

        var savedPaymentMethod = paymentMethodRepository.save(paymentMethod);

        // Link to user
        user.setPaymentMethod(savedPaymentMethod);
        userRepository.save(user);

        return Optional.of(savedPaymentMethod);
    }

    @Override
    @Transactional
    public Optional<PaymentMethod> handle(UpdatePaymentMethodCommand command) {
        // Validate card number
        if (!CardTypeDetector.isValidCardNumber(command.cardNumber())) {
            throw new IllegalArgumentException("Invalid card number");
        }

        // Detect card type
        String cardType = CardTypeDetector.detectCardType(command.cardNumber());

        // Validate CVV
        if (!CardTypeDetector.isValidCVV(command.cvv(), cardType)) {
            throw new IllegalArgumentException("Invalid CVV");
        }

        // Validate expiry
        if (!CardTypeDetector.isValidExpiry(command.expiryMonth(), command.expiryYear())) {
            throw new IllegalArgumentException("Invalid or expired card");
        }

        // Get user
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user has a payment method
        if (user.getPaymentMethod() == null) {
            throw new IllegalStateException("No payment method found. Use add instead.");
        }

        var existingPaymentMethod = user.getPaymentMethod();

        // Update payment method
        existingPaymentMethod.setCardholderName(command.cardholderName());
        existingPaymentMethod.setLastFourDigits(command.cardNumber().substring(command.cardNumber().length() - 4));
        existingPaymentMethod.setCardType(cardType);
        existingPaymentMethod.setExpiryMonth(command.expiryMonth());
        existingPaymentMethod.setExpiryYear(command.expiryYear());

        var updatedPaymentMethod = paymentMethodRepository.save(existingPaymentMethod);

        return Optional.of(updatedPaymentMethod);
    }

    @Override
    @Transactional
    public void handle(DeletePaymentMethodCommand command) {
        // Get user
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if user has a payment method
        if (user.getPaymentMethod() == null) {
            throw new IllegalStateException("No payment method found");
        }

        var paymentMethod = user.getPaymentMethod();

        // Unlink from user first
        user.setPaymentMethod(null);
        userRepository.save(user);

        // Delete payment method
        paymentMethodRepository.delete(paymentMethod);
    }
}