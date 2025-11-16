package com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources;

public record UpdateMonitoringResource(
    Long medicineId,
    String medicineName,
    Double temperatura,
    String estado,
    Integer stock,
    String ubicacion
) {}

