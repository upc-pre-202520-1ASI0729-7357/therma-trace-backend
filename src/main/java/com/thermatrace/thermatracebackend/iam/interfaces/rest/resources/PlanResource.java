package com.thermatrace.thermatracebackend.iam.interfaces.rest.resources;

import java.util.List;

public record PlanResource(
        String id,
        String name,
        double price,
        List<String> features
) {}
