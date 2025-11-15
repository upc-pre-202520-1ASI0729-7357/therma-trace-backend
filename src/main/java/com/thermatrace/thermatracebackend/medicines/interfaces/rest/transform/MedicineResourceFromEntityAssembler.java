package com.thermatrace.thermatracebackend.medicines.interfaces.rest.transform;

import com.thermatrace.thermatracebackend.medicines.domain.model.aggregates.Medicine;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.resources.MedicineResource;

public class MedicineResourceFromEntityAssembler {
    public static MedicineResource toResourceFromEntity(Medicine entity) {
        return new MedicineResource(
                entity.getId(),
                entity.getName(),
                entity.getExpirationDate(),
                entity.getImageUrlValue()
        );
    }
}
