package com.thermatrace.thermatracebackend.medicines.interfaces.rest.transform;

import com.thermatrace.thermatracebackend.medicines.domain.model.commands.UpdateMedicineCommand;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.resources.UpdateMedicineResource;

public class UpdateMedicineCommandFromResourceAssembler {
    public static UpdateMedicineCommand toCommandFromResource(Long userId, Long medicineId, UpdateMedicineResource resource) {
        return new UpdateMedicineCommand(
                userId,
                medicineId,
                resource.name(),
                resource.expirationDate(),
                resource.imageUrl()
        );
    }
}
