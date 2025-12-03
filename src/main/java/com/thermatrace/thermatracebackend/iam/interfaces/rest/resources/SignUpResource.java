package com.thermatrace.thermatracebackend.iam.interfaces.rest.resources;

import java.util.List;

public record SignUpResource(
        String firstName,
        String lastName,
        String email,
        String password,
        List<String> roles
) {}