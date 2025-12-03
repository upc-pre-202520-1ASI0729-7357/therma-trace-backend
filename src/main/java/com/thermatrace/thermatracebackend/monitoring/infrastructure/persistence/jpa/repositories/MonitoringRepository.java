package com.thermatrace.thermatracebackend.monitoring.infrastructure.persistence.jpa.repositories;

import com.thermatrace.thermatracebackend.monitoring.domain.model.aggregates.Monitoring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonitoringRepository extends JpaRepository<Monitoring, Long> {
    List<Monitoring> findAllByUserId(Long userId);
    Optional<Monitoring> findByIdAndUserId(Long id, Long userId);
}
