package com.thermatrace.thermatracebackend.iam.domain.model.commands;

public record UpdateProfileCommand(
    Long userId,
    String firstName,
    String lastName,
    String phone,
    String timezoneId,
    String languageId
) {}