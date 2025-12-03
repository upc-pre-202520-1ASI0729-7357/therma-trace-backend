package com.thermatrace.thermatracebackend.iam.interfaces.transform;

import com.thermatrace.thermatracebackend.iam.domain.model.commands.SignUpCommand;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.SignUpResource;

public class SignUpCommandFromResourceAssembler {

    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        return new SignUpCommand(
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.password(),
                resource.roles()
        );
    }
}
