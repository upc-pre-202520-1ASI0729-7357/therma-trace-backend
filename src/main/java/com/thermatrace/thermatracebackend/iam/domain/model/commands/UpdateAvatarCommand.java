package com.thermatrace.thermatracebackend.iam.domain.model.commands;

public record UpdateAvatarCommand(
    Long userId,
    String avatarUrl
) {}