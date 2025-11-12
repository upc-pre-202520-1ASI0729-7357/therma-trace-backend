package com.thermatrace.thermatracebackend.iam.interfaces.transform;

import com.thermatrace.thermatracebackend.iam.domain.model.commands.SignInCommand;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {

    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(
                resource.email(),
                resource.password()
        );
    }
}
