package com.thermatrace.thermatracebackend.iam.interfaces.rest.resources;

public record ProfileResource(
        Long id,
        String fullName,      // Computed: firstName + " " + lastName
        String email,
        String phone,
        String avatar,
        String role,
        String timezoneId,
        String planId,        // Mapped from currentPlan
        String languageId,
        Long paymentMethodId
) {}
