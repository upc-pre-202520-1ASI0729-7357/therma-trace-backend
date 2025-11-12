package com.thermatrace.thermatracebackend.iam.interfaces.rest.resources;

public record TimezoneResource(
        String id,
        String name,
        String offset
) {}
