package com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources;

public record UpdateMonitoringResource(
    Long medicineId,
    Double temperature,
    String state,
    Integer stock,
    String location
) {}

