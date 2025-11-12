package com.thermatrace.thermatracebackend.iam.interfaces.rest.resources;

public record UpdateProfileResource(
        String phone,
        String timezoneId,
        String languageId
) {}
