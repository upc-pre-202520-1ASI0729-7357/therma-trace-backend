package com.thermatrace.thermatracebackend.medicines.application.internal.commandservices;

import com.thermatrace.thermatracebackend.medicines.domain.model.aggregates.Medicine;
import com.thermatrace.thermatracebackend.medicines.domain.model.commands.CreateMedicineCommand;
import com.thermatrace.thermatracebackend.medicines.domain.model.commands.DeleteMedicineCommand;
import com.thermatrace.thermatracebackend.medicines.domain.model.commands.UpdateMedicineCommand;
import com.thermatrace.thermatracebackend.medicines.domain.services.MedicineCommandService;
import com.thermatrace.thermatracebackend.medicines.infrastructure.persistence.jpa.repositories.MedicineRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicineCommandServiceImpl implements MedicineCommandService {

    private final MedicineRepository medicineRepository;

    public MedicineCommandServiceImpl(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public Optional<Medicine> handle(CreateMedicineCommand command) {
        try {
            var medicine = new Medicine(
                    command.userId(),
                    command.name(),
                    command.expirationDate(),
                    command.imageUrl()
            );
            return Optional.of(medicineRepository.save(medicine));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating medicine: " + e.getMessage());
        }
    }

    @Override
    public Optional<Medicine> handle(UpdateMedicineCommand command) {
        // Verify the medicine belongs to the user before updating
        return medicineRepository.findByIdAndUserId(command.id(), command.userId())
                .map(medicine -> {
                    medicine.updateMedicine(
                            command.name(),
                            command.expirationDate(),
                            command.imageUrl()
                    );
                    return medicineRepository.save(medicine);
                });
    }

    @Override
    public void handle(DeleteMedicineCommand command) {
        // This will be updated by controller to verify userId
        if (medicineRepository.existsById(command.id())) {
            medicineRepository.deleteById(command.id());
        } else {
            throw new IllegalArgumentException("Medicine with id " + command.id() + " not found");
        }
    }
}
