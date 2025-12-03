package com.thermatrace.thermatracebackend.monitoring.interfaces.rest.transform;

import com.thermatrace.thermatracebackend.monitoring.domain.model.commands.CreateMonitoringCommand;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources.CreateMonitoringResource;
import org.springframework.stereotype.Component;

@Component
public class CreateMonitoringCommandFromResourceAssembler {
    public CreateMonitoringCommand toCommand(Long userId, CreateMonitoringResource resource) {
        return new CreateMonitoringCommand(
            userId,
            resource.medicineId(),
            resource.temperature(),
            resource.state(),
            resource.stock(),
            resource.location()
        );
    }
}
