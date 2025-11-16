package com.thermatrace.thermatracebackend.monitoring.application.internal.commandservices;

import com.thermatrace.thermatracebackend.monitoring.domain.model.aggregates.Monitoring;
import com.thermatrace.thermatracebackend.monitoring.domain.model.commands.CreateMonitoringCommand;
import com.thermatrace.thermatracebackend.monitoring.domain.model.commands.UpdateMonitoringCommand;
import com.thermatrace.thermatracebackend.monitoring.domain.model.commands.DeleteMonitoringCommand;
import com.thermatrace.thermatracebackend.monitoring.domain.services.MonitoringCommandService;
import com.thermatrace.thermatracebackend.monitoring.infrastructure.persistence.jpa.repositories.MonitoringRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Service
public class MonitoringCommandServiceImpl implements MonitoringCommandService {
    private final MonitoringRepository monitoringRepository;

    @Autowired
    public MonitoringCommandServiceImpl(MonitoringRepository monitoringRepository) {
        this.monitoringRepository = monitoringRepository;
    }

    @Override
    public Optional<Monitoring> handle(CreateMonitoringCommand command) {
        Monitoring monitoring = new Monitoring(
            command.medicineId(),
            command.temperature(),
            command.state(),
            command.stock(),
            command.location()
        );
        return Optional.of(monitoringRepository.save(monitoring));
    }

    @Override
    public Optional<Monitoring> handle(UpdateMonitoringCommand command) {
        return monitoringRepository.findById(command.id()).map(existing -> {
            existing.setMedicineId(command.medicineId());
            existing.setTemperature(command.temperature());
            existing.setState(command.state());
            existing.setStock(command.stock());
            existing.setLocation(command.location());
            return monitoringRepository.save(existing);
        });
    }

    @Override
    public void handle(DeleteMonitoringCommand command) {
        monitoringRepository.deleteById(command.id());
    }
}
