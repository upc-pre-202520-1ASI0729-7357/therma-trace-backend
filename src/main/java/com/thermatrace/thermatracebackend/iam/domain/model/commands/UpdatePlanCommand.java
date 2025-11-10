package com.thermatrace.thermatracebackend.iam.domain.model.commands;

import com.thermatrace.thermatracebackend.iam.domain.model.valueobjects.Plans;

public record UpdatePlanCommand(
    Long userId,
    Plans newPlan
) {}