package com.thermatrace.thermatracebackend.iam.interfaces.rest.resources;

public record UserResource(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String avatar,
        String timezoneId,
        String languageId,
        String currentPlan,
        String role
) {}