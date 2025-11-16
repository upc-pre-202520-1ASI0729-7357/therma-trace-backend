package com.thermatrace.thermatracebackend.monitoring.interfaces.rest.transform;

import com.thermatrace.thermatracebackend.monitoring.domain.model.aggregates.Monitoring;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources.MonitoringResource;
import org.springframework.stereotype.Component;

@Component
public class MonitoringResourceFromEntityAssembler {
    public MonitoringResource toResource(Monitoring monitoring) {
        return new MonitoringResource(
            monitoring.getId(),
            monitoring.getMedicineId(),
            monitoring.getMedicineName(),
            monitoring.getTemperatura(),
            monitoring.getEstado(),
            monitoring.getStock(),
            monitoring.getUbicacion()
        );
    }
}
