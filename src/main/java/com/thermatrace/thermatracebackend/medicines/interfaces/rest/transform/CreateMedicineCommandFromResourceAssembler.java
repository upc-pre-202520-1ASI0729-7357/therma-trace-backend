package com.thermatrace.thermatracebackend.medicines.interfaces.rest.transform;

import com.thermatrace.thermatracebackend.medicines.domain.model.commands.CreateMedicineCommand;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.resources.CreateMedicineResource;

public class CreateMedicineCommandFromResourceAssembler {
    public static CreateMedicineCommand toCommandFromResource(CreateMedicineResource resource) {
        return new CreateMedicineCommand(
                resource.name(),
                resource.expirationDate(),
                resource.imageUrl()
        );
    }
}
