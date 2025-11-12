package com.thermatrace.thermatracebackend.iam.interfaces.transform;

import com.thermatrace.thermatracebackend.iam.domain.model.aggregates.User;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {

    public static ProfileResource toResourceFromEntity(User user) {
        return new ProfileResource(
                user.getId(),
                user.getFullName(),  // firstName + " " + lastName
                user.getEmail(),
                user.getPhone(),
                user.getAvatar(),
                user.getRoles().isEmpty() ? null : user.getRoles().iterator().next().getStringName(),
                user.getTimezoneId(),
                user.getCurrentPlan().name(),  // Enum to string
                user.getLanguageId(),
                user.getPaymentMethod() != null ? user.getPaymentMethod().getId() : null
        );
    }
}
