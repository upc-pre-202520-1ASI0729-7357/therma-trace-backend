package com.thermatrace.thermatracebackend.medicines.domain.model.commands;

import java.time.LocalDate;

public record UpdateMedicineCommand(
        Long id,
        String name,
        LocalDate expirationDate,
        String imageUrl
) {
}
