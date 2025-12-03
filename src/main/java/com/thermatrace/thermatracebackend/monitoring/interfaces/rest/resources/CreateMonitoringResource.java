package com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources;

public record CreateMonitoringResource(
    Long medicineId,
    Double temperature,
    String state,
    Integer stock,
    String location
) {}
