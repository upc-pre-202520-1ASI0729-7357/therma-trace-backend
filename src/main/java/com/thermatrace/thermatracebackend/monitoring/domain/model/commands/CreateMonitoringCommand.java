package com.thermatrace.thermatracebackend.monitoring.domain.model.commands;

public record CreateMonitoringCommand(
    Long medicineId,
    String medicineName,
    Double temperatura,
    String estado,
    Integer stock,
    String ubicacion
) {}
