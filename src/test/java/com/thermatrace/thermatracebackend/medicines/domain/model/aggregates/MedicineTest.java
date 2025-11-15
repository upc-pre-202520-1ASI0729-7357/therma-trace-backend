package com.thermatrace.thermatracebackend.medicines.domain.model.aggregates;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class MedicineTest {

    @Test
    public void testMedicineCreation() {
        // Given
        String name = "Acetaminofen";
        LocalDate expirationDate = LocalDate.of(2024, 12, 31);
        String imageUrl = "https://example.com/image.jpg";

        // When
        Medicine medicine = new Medicine(name, expirationDate, imageUrl);

        // Then
        assertEquals(name, medicine.getName());
        assertEquals(expirationDate, medicine.getExpirationDate());
        assertEquals(imageUrl, medicine.getImageUrlValue());
    }

    @Test
    public void testMedicineUpdate() {
        // Given
        Medicine medicine = new Medicine("Original", LocalDate.of(2024, 6, 30), "https://original.com");

        // When
        medicine.updateMedicine("Updated", LocalDate.of(2024, 12, 31), "https://updated.com");

        // Then
        assertEquals("Updated", medicine.getName());
        assertEquals(LocalDate.of(2024, 12, 31), medicine.getExpirationDate());
        assertEquals("https://updated.com", medicine.getImageUrlValue());
    }
}

