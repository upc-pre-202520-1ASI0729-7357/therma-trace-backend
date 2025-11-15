package com.thermatrace.thermatracebackend.medicines.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record MedicineResource(
        Long id,
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate expirationDate,
        String imageUrl
) {
}
