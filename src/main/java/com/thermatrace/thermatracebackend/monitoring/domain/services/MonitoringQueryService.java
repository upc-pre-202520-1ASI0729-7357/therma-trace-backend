package com.thermatrace.thermatracebackend.monitoring.domain.services;

import com.thermatrace.thermatracebackend.monitoring.domain.model.aggregates.Monitoring;
import com.thermatrace.thermatracebackend.monitoring.domain.model.queries.GetAllMonitoringsByUserIdQuery;
import com.thermatrace.thermatracebackend.monitoring.domain.model.queries.GetMonitoringByIdAndUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface MonitoringQueryService {
    List<Monitoring> getAll();
    Optional<Monitoring> getById(Long id);
    List<Monitoring> handle(GetAllMonitoringsByUserIdQuery query);
    Optional<Monitoring> handle(GetMonitoringByIdAndUserIdQuery query);
}
