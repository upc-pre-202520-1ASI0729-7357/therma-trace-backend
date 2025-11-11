package com.thermatrace.thermatracebackend.shared.interfaces.rest.resources;

import java.time.LocalDateTime;

public record ErrorResource(
    int status,
    String message,
    LocalDateTime timestamp
) {}