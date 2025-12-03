package com.thermatrace.thermatracebackend.monitoring.application.internal.queryservices;

import com.thermatrace.thermatracebackend.monitoring.domain.model.aggregates.Monitoring;
import com.thermatrace.thermatracebackend.monitoring.domain.model.queries.GetAllMonitoringsByUserIdQuery;
import com.thermatrace.thermatracebackend.monitoring.domain.model.queries.GetMonitoringByIdAndUserIdQuery;
import com.thermatrace.thermatracebackend.monitoring.domain.services.MonitoringQueryService;
import com.thermatrace.thermatracebackend.monitoring.infrastructure.persistence.jpa.repositories.MonitoringRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

@Service
public class MonitoringQueryServiceImpl implements MonitoringQueryService {
    private final MonitoringRepository monitoringRepository;

    @Autowired
    public MonitoringQueryServiceImpl(MonitoringRepository monitoringRepository) {
        this.monitoringRepository = monitoringRepository;
    }

    @Override
    public List<Monitoring> getAll() {
        return monitoringRepository.findAll();
    }

    @Override
    public Optional<Monitoring> getById(Long id) {
        return monitoringRepository.findById(id);
    }

    @Override
    public List<Monitoring> handle(GetAllMonitoringsByUserIdQuery query) {
        return monitoringRepository.findAllByUserId(query.userId());
    }

    @Override
    public Optional<Monitoring> handle(GetMonitoringByIdAndUserIdQuery query) {
        return monitoringRepository.findByIdAndUserId(query.id(), query.userId());
    }
}
