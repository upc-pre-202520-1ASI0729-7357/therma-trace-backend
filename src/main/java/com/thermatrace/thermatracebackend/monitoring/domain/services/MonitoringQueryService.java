package com.thermatrace.thermatracebackend.monitoring.domain.services;

import com.thermatrace.thermatracebackend.monitoring.domain.model.aggregates.Monitoring;
import java.util.List;
import java.util.Optional;

public interface MonitoringQueryService {
    List<Monitoring> getAll();
    Optional<Monitoring> getById(Long id);
}
