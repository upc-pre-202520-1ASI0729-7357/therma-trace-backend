package com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources;

import java.time.LocalDate;

public record MedicineDetailsResource(
    Long id,
    String name,
    LocalDate expirationDate,
    String image
) {}
