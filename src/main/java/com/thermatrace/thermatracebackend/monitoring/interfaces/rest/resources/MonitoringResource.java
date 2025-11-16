package com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources;

public record MonitoringResource(
    Long id,
    Double temperature,
    String state,
    Integer stock,
    String location,
    MedicineDetailsResource medicine
) {}
