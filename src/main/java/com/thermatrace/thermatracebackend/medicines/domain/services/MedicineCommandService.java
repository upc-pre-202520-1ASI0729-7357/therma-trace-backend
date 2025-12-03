package com.thermatrace.thermatracebackend.medicines.domain.services;

import com.thermatrace.thermatracebackend.medicines.domain.model.aggregates.Medicine;
import com.thermatrace.thermatracebackend.medicines.domain.model.commands.CreateMedicineCommand;
import com.thermatrace.thermatracebackend.medicines.domain.model.commands.DeleteMedicineCommand;
import com.thermatrace.thermatracebackend.medicines.domain.model.commands.UpdateMedicineCommand;

import java.util.Optional;

public interface MedicineCommandService {
    Optional<Medicine> handle(CreateMedicineCommand command);
    Optional<Medicine> handle(UpdateMedicineCommand command);
    void handle(DeleteMedicineCommand command);
}
