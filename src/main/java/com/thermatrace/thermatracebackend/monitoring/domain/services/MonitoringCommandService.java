package com.thermatrace.thermatracebackend.monitoring.domain.services;

import com.thermatrace.thermatracebackend.monitoring.domain.model.aggregates.Monitoring;
import com.thermatrace.thermatracebackend.monitoring.domain.model.commands.CreateMonitoringCommand;
import com.thermatrace.thermatracebackend.monitoring.domain.model.commands.UpdateMonitoringCommand;
import com.thermatrace.thermatracebackend.monitoring.domain.model.commands.DeleteMonitoringCommand;
import java.util.Optional;

public interface MonitoringCommandService {
    Optional<Monitoring> handle(CreateMonitoringCommand command);
    Optional<Monitoring> handle(UpdateMonitoringCommand command);
    void handle(DeleteMonitoringCommand command);
}
