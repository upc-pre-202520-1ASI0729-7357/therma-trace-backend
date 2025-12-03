package com.thermatrace.thermatracebackend.iam.domain.model.commands;

import com.thermatrace.thermatracebackend.iam.domain.model.valueobjects.Plans;

public record UpdateProfileCommand(
    Long userId,
    String firstName,
    String lastName,
    String phone,
    String timezoneId,
    String languageId,
    Plans currentPlan
) {}