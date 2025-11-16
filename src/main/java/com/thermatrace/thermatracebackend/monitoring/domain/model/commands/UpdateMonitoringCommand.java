package com.thermatrace.thermatracebackend.monitoring.domain.model.commands;

public record UpdateMonitoringCommand(
    Long id,
    Long medicineId,
    Double temperature,
    String state,
    Integer stock,
    String location
) {}
