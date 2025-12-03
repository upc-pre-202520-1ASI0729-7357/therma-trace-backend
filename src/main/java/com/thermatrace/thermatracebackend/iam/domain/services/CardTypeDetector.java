package com.thermatrace.thermatracebackend.iam.domain.services;

public class CardTypeDetector {

    public static String detectCardType(String cardNumber) {
        // Remove spaces and dashes
        String cleanedNumber = cardNumber.replaceAll("[\\s-]", "");

        // Visa: starts with 4
        if (cleanedNumber.matches("^4[0-9]{12}(?:[0-9]{3})?$")) {
            return "VISA";
        }

        // Mastercard: starts with 51-55 or 2221-2720
        if (cleanedNumber.matches("^5[1-5][0-9]{14}$") ||
            cleanedNumber.matches("^2(?:2(?:2[1-9]|[3-9][0-9])|[3-6][0-9]{2}|7(?:[0-1][0-9]|20))[0-9]{12}$")) {
            return "MASTERCARD";
        }

        // American Express: starts with 34 or 37
        if (cleanedNumber.matches("^3[47][0-9]{13}$")) {
            return "AMEX";
        }

        // Discover: starts with 6011, 622126-622925, 644-649, or 65
        if (cleanedNumber.matches("^6(?:011|5[0-9]{2})[0-9]{12}$") ||
            cleanedNumber.matches("^62212[6-9][0-9]{12}$") ||
            cleanedNumber.matches("^6229[01][0-9]{12}$") ||
            cleanedNumber.matches("^62292[0-5][0-9]{11}$") ||
            cleanedNumber.matches("^64[4-9][0-9]{13}$")) {
            return "DISCOVER";
        }

        // Default to UNKNOWN if we can't detect
        return "UNKNOWN";
    }

    public static boolean isValidCardNumber(String cardNumber) {
        String cleanedNumber = cardNumber.replaceAll("[\\s-]", "");

        // Basic length check (13-19 digits for most cards)
        if (!cleanedNumber.matches("^[0-9]{13,19}$")) {
            return false;
        }

        // Luhn algorithm validation
        return luhnCheck(cleanedNumber);
    }

    private static boolean luhnCheck(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(cardNumber.substring(i, i + 1));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }

    public static boolean isValidCVV(String cvv, String cardType) {
        if (cvv == null || cvv.isEmpty()) {
            return false;
        }

        // AMEX uses 4-digit CVV, others use 3-digit
        if ("AMEX".equals(cardType)) {
            return cvv.matches("^[0-9]{4}$");
        } else {
            return cvv.matches("^[0-9]{3}$");
        }
    }

    public static boolean isValidExpiry(String month, String year) {
        if (month == null || year == null) {
            return false;
        }

        // Month should be 01-12
        if (!month.matches("^(0[1-9]|1[0-2])$")) {
            return false;
        }

        // Year should be 4 digits
        if (!year.matches("^[0-9]{4}$")) {
            return false;
        }

        // Check if not expired
        int currentYear = java.time.Year.now().getValue();
        int currentMonth = java.time.MonthDay.now().getMonthValue();
        int expYear = Integer.parseInt(year);
        int expMonth = Integer.parseInt(month);

        if (expYear < currentYear) {
            return false;
        }

        if (expYear == currentYear && expMonth < currentMonth) {
            return false;
        }

        return true;
    }
}