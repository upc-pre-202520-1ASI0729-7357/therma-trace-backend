package com.thermatrace.thermatracebackend.medicines.domain.model.commands;

import java.time.LocalDate;

public record CreateMedicineCommand(
        Long userId,
        String name,
        LocalDate expirationDate,
        String imageUrl
) {
}
