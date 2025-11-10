package com.thermatrace.thermatracebackend.iam.domain.services;

import com.thermatrace.thermatracebackend.iam.domain.model.aggregates.User;
import com.thermatrace.thermatracebackend.iam.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery query);
    Optional<User> handle(GetUserByEmailQuery query);
    List<User> handle(GetAllUsersQuery query);
}