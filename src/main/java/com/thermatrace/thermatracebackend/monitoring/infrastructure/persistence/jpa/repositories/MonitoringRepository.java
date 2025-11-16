package com.thermatrace.thermatracebackend.monitoring.infrastructure.persistence.jpa.repositories;

import com.thermatrace.thermatracebackend.monitoring.domain.model.aggregates.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoringRepository extends JpaRepository<Monitoring, Long> {
}
