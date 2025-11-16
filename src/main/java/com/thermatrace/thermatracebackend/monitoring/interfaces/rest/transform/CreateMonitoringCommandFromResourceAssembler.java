package com.thermatrace.thermatracebackend.monitoring.interfaces.rest.transform;

import com.thermatrace.thermatracebackend.monitoring.domain.model.commands.CreateMonitoringCommand;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources.CreateMonitoringResource;
import org.springframework.stereotype.Component;

@Component
public class CreateMonitoringCommandFromResourceAssembler {
    public CreateMonitoringCommand toCommand(CreateMonitoringResource resource) {
        return new CreateMonitoringCommand(
            resource.medicineId(),
            resource.medicineName(),
            resource.temperatura(),
            resource.estado(),
            resource.stock(),
            resource.ubicacion()
        );
    }
}
