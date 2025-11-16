package com.thermatrace.thermatracebackend.monitoring.interfaces.rest.transform;

import com.thermatrace.thermatracebackend.monitoring.domain.model.aggregates.Monitoring;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources.MonitoringResource;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources.MedicineDetailsResource;
import com.thermatrace.thermatracebackend.medicines.infrastructure.persistence.jpa.repositories.MedicineRepository;
import org.springframework.stereotype.Component;

@Component
public class MonitoringResourceFromEntityAssembler {

    private final MedicineRepository medicineRepository;

    public MonitoringResourceFromEntityAssembler(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public MonitoringResource toResource(Monitoring monitoring) {
        var medicine = medicineRepository.findById(monitoring.getMedicineId())
                .orElse(null);

        MedicineDetailsResource medicineDetails = null;
        if (medicine != null) {
            medicineDetails = new MedicineDetailsResource(
                medicine.getId(),
                medicine.getName(),
                medicine.getExpirationDate(),
                medicine.getImageUrlValue()
            );
        }

        return new MonitoringResource(
            monitoring.getId(),
            monitoring.getTemperature(),
            monitoring.getState(),
            monitoring.getStock(),
            monitoring.getLocation(),
            medicineDetails
        );
    }
}
