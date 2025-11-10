package com.thermatrace.thermatracebackend.iam.domain.model.commands;

import java.util.List;

public record SignUpCommand(
    String firstName,
    String lastName,
    String email,
    String password,
    List<String> roles
) {}