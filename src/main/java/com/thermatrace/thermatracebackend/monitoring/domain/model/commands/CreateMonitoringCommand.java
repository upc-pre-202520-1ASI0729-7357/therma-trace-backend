package com.thermatrace.thermatracebackend.monitoring.domain.model.commands;

public record CreateMonitoringCommand(
    Long medicineId,
    Double temperature,
    String state,
    Integer stock,
    String location
) {}
