package com.thermatrace.thermatracebackend.medicines.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class MedicineName {
    private final String name;

    public MedicineName() {
        this.name = "";
    }

    public MedicineName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Medicine name cannot be null or empty");
        }
        this.name = name.trim();
    }

    @Override
    public String toString() {
        return name;
    }
}
