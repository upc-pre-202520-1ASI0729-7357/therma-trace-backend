package com.thermatrace.thermatracebackend.medicines.application.internal.commandservices;

import com.thermatrace.thermatracebackend.medicines.domain.model.aggregates.Medicine;
import com.thermatrace.thermatracebackend.medicines.domain.model.commands.CreateMedicineCommand;
import com.thermatrace.thermatracebackend.medicines.domain.model.commands.DeleteMedicineCommand;
import com.thermatrace.thermatracebackend.medicines.domain.model.commands.UpdateMedicineCommand;
import com.thermatrace.thermatracebackend.medicines.domain.services.MedicineCommandService;
import com.thermatrace.thermatracebackend.medicines.infrastructure.persistence.jpa.repositories.MedicineRepository;
import com.thermatrace.thermatracebackend.monitoring.domain.model.aggregates.Monitoring;
import com.thermatrace.thermatracebackend.monitoring.infrastructure.persistence.jpa.repositories.MonitoringRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicineCommandServiceImpl implements MedicineCommandService {

    private final MedicineRepository medicineRepository;
    private final MonitoringRepository monitoringRepository;

    public MedicineCommandServiceImpl(MedicineRepository medicineRepository, MonitoringRepository monitoringRepository) {
        this.medicineRepository = medicineRepository;
        this.monitoringRepository = monitoringRepository;
    }

    @Override
    public Optional<Medicine> handle(CreateMedicineCommand command) {
        try {
            // Create medicine
            var medicine = new Medicine(
                    command.userId(),
                    command.name(),
                    command.expirationDate(),
                    command.imageUrl()
            );
            var savedMedicine = medicineRepository.save(medicine);

            // Determine initial state based on expiration date
            String initialState = command.expirationDate().isBefore(java.time.LocalDate.now())
                    ? "inactive"  // Expired
                    : "active";   // Not expired

            // Auto-create default monitoring entry for the new medicine
            var monitoring = new Monitoring(
                    command.userId(),
                    savedMedicine.getId(),
                    0.0,           // Default temperature
                    initialState,  // State based on expiration
                    0,             // Default stock
                    "Not set"      // Default location
            );
            monitoringRepository.save(monitoring);

            return Optional.of(savedMedicine);
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
                    var updatedMedicine = medicineRepository.save(medicine);

                    // Update monitoring state based on expiration date
                    monitoringRepository.findByMedicineIdAndUserId(command.id(), command.userId())
                            .ifPresent(monitoring -> {
                                // Determine state based on expiration date
                                String newState = command.expirationDate().isBefore(java.time.LocalDate.now())
                                        ? "inactive"  // Expired
                                        : "active";   // Not expired
                                monitoring.setState(newState);
                                monitoringRepository.save(monitoring);
                            });

                    return updatedMedicine;
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
