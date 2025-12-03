package com.thermatrace.thermatracebackend.iam.interfaces.rest.resources;

public record SignInResource(
        String email,
        String password
) {}