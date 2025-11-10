package com.thermatrace.thermatracebackend.iam.domain.services;

import com.thermatrace.thermatracebackend.iam.domain.model.aggregates.User;
import com.thermatrace.thermatracebackend.iam.domain.model.commands.*;

import java.util.Optional;

public interface UserCommandService {
    Long handle(SignUpCommand command);
    Optional<String> handle(SignInCommand command);
    void handle(UpdateProfileCommand command);
    void handle(SavePaymentMethodCommand command);
    void handle(UpdatePlanCommand command);
    void handle(ForgotPasswordCommand command);
    void handle(UpdateAvatarCommand command);
}