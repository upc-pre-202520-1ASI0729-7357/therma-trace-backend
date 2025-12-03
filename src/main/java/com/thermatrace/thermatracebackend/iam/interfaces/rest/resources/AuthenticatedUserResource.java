package com.thermatrace.thermatracebackend.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(
        Long id,
        String email,
        String firstName,
        String lastName,
        String token
) {}