package com.thermatrace.thermatracebackend.iam.interfaces.rest.resources;

import com.thermatrace.thermatracebackend.iam.domain.model.valueobjects.Plans;

public record UpdateProfileResource(
        String phone,
        String timezoneId,
        String languageId,
        Plans currentPlan
) {}
