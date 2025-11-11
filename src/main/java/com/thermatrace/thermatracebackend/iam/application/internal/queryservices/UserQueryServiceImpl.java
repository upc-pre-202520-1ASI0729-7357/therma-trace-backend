package com.thermatrace.thermatracebackend.iam.application.internal.queryservices;

import com.thermatrace.thermatracebackend.iam.domain.model.aggregates.User;
import com.thermatrace.thermatracebackend.iam.domain.model.queries.*;
import com.thermatrace.thermatracebackend.iam.domain.services.UserQueryService;
import com.thermatrace.thermatracebackend.iam.infrastructure.persistence.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery query) {
        return userRepository.findByEmail(query.email());
    }

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }
}