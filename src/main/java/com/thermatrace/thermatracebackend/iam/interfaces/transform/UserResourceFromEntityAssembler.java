package com.thermatrace.thermatracebackend.iam.interfaces.transform;

import com.thermatrace.thermatracebackend.iam.domain.model.aggregates.User;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {

    public static UserResource toResourceFromEntity(User user) {
        return new UserResource(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getAvatar(),
                user.getTimezoneId(),
                user.getLanguageId(),
                user.getCurrentPlan().name(),
                user.getRoles().isEmpty() ? null : user.getRoles().iterator().next().getStringName()
        );
    }
}
