package com.thermatrace.thermatracebackend.iam.domain.model.valueobjects;

import java.util.List;

public enum Plans {
    FREEMIUM(
        0,
        "Freemium",
        "Basic features",
        5,
        false,
        false,
        1  // Weekly alerts
    ),
    PREMIUM(
        59.99,
        "Premium",
        "All features with unlimited capacity",
        100,
        true,  // Daily alerts
        true,  // Excel reports on demand
        7      // Daily alerts
    ),
    ENTERPRISE(
        99.99,
        "Enterprise",
        "Complete solution with scheduled reports",
        999,
        true,  // Daily alerts
        true,  // All report types + scheduling
        7      // Daily alerts
    );

    private final double price;
    private final String displayName;
    private final String description;
    private final int medicamentLimit;
    private final boolean hasDailyAlerts;
    private final boolean hasAdvancedReports;
    private final int alertFrequencyDays;

    Plans(double price, String displayName, String description,
          int medicamentLimit, boolean hasDailyAlerts,
          boolean hasAdvancedReports, int alertFrequencyDays) {
        this.price = price;
        this.displayName = displayName;
        this.description = description;
        this.medicamentLimit = medicamentLimit;
        this.hasDailyAlerts = hasDailyAlerts;
        this.hasAdvancedReports = hasAdvancedReports;
        this.alertFrequencyDays = alertFrequencyDays;
    }

    public double getPrice() {
        return price;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getMedicamentLimit() {
        return medicamentLimit;
    }

    public boolean hasDailyAlerts() {
        return hasDailyAlerts;
    }

    public boolean hasAdvancedReports() {
        return hasAdvancedReports;
    }

    public int getAlertFrequencyDays() {
        return alertFrequencyDays;
    }

    public boolean canAddMedicament(int currentCount) {
        return currentCount < medicamentLimit;
    }

    public List<String> getFeatures() {
        return switch (this) {
            case FREEMIUM -> List.of(
                "Up to 5 medicaments",
                "Weekly basic alerts",
                "Temperature monitoring",
                "Expiration tracking"
            );
            case PREMIUM -> List.of(
                "Up to 15 medicaments",
                "Daily alerts",
                "Temperature monitoring",
                "Expiration tracking",
                "Advanced charts",
                "Excel reports on demand"
            );
            case ENTERPRISE -> List.of(
                "Unlimited medicaments",
                "Daily alerts",
                "Temperature monitoring",
                "Expiration tracking",
                "Advanced charts",
                "Excel reports on demand",
                "Scheduled reports",
                "Priority support"
            );
        };
    }
}