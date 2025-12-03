package com.thermatrace.thermatracebackend.iam.domain.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MOCK Payment Method - Storage only, no real payment processing
 * Stores only last 4 digits of card number for security
 */
@Entity
@Table(name = "payment_methods")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardholderName;

    @Column(nullable = false, length = 4)
    private String lastFourDigits; // Store only last 4 digits

    @Column(nullable = false)
    private String cardType; // VISA, MASTERCARD, AMEX, etc.

    @Column(nullable = false, length = 2)
    private String expiryMonth;

    @Column(nullable = false, length = 4)
    private String expiryYear;

    // CVV is NEVER stored - only validated on input

    public String getMaskedCardNumber() {
        return "**** **** **** " + lastFourDigits;
    }

    public String getExpiry() {
        return expiryMonth + "/" + expiryYear;
    }

    public static PaymentMethod fromCardNumber(String cardholderName, String fullCardNumber,
                                                String cardType, String expiryMonth, String expiryYear) {
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setCardholderName(cardholderName);
        paymentMethod.setLastFourDigits(fullCardNumber.substring(fullCardNumber.length() - 4));
        paymentMethod.setCardType(cardType);
        paymentMethod.setExpiryMonth(expiryMonth);
        paymentMethod.setExpiryYear(expiryYear);
        return paymentMethod;
    }
}