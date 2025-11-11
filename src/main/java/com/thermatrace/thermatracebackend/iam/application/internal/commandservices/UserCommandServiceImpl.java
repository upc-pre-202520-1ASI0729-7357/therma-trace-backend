package com.thermatrace.thermatracebackend.iam.application.internal.commandservices;

import com.thermatrace.thermatracebackend.iam.application.internal.outboundservices.EmailService;
import com.thermatrace.thermatracebackend.iam.application.internal.outboundservices.HashingService;
import com.thermatrace.thermatracebackend.iam.application.internal.outboundservices.TokenService;
import com.thermatrace.thermatracebackend.iam.domain.model.aggregates.User;
import com.thermatrace.thermatracebackend.iam.domain.model.commands.*;
import com.thermatrace.thermatracebackend.iam.domain.model.entities.PaymentMethod;
import com.thermatrace.thermatracebackend.iam.domain.model.entities.Role;
import com.thermatrace.thermatracebackend.iam.domain.services.UserCommandService;
import com.thermatrace.thermatracebackend.iam.infrastructure.persistence.jpa.RoleRepository;
import com.thermatrace.thermatracebackend.iam.infrastructure.persistence.jpa.UserRepository;
import com.thermatrace.thermatracebackend.shared.domain.exceptions.ResourceNotFoundException;
import com.thermatrace.thermatracebackend.shared.domain.exceptions.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * User command service implementation
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final EmailService emailService;

    public UserCommandServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                                   HashingService hashingService, TokenService tokenService,
                                   EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    /**
     * Handle sign-up command
     * Creates a new user with ROLE_USER and FREEMIUM plan by default
     */
    @Override
    @Transactional
    public Long handle(SignUpCommand command) {
        // Validate email doesn't exist
        if (userRepository.existsByEmail(command.email())) {
            throw new ValidationException("Email already exists");
        }

        // Create new user
        User user = new User(
                command.firstName(),
                command.lastName(),
                command.email(),
                hashingService.encode(command.password())
        );

        // Assign roles (default to ROLE_USER if not provided)
        Set<Role> roles = new HashSet<>();
        if (command.roles() == null || command.roles().isEmpty()) {
            Role userRole = roleRepository.findByName(com.thermatrace.thermatracebackend.iam.domain.model.valueobjects.Roles.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
            roles.add(userRole);
        } else {
            for (String roleName : command.roles()) {
                Role role = roleRepository.findByName(com.thermatrace.thermatracebackend.iam.domain.model.valueobjects.Roles.valueOf(roleName))
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
                roles.add(role);
            }
        }
        user.getRoles().addAll(roles);

        // Save user
        User savedUser = userRepository.save(user);

        // Send welcome email
        emailService.sendWelcomeEmail(savedUser.getEmail(), savedUser.getFirstName());

        return savedUser.getId();
    }

    /**
     * Handle sign-in command
     * Returns the JWT token if credentials are valid
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<String> handle(SignInCommand command) {
        // Find user by email
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate password
        if (!hashingService.matches(command.password(), user.getPassword())) {
            throw new ValidationException("Invalid credentials");
        }

        // Generate token
        String token = tokenService.generateToken(user.getEmail());

        return Optional.of(token);
    }

    /**
     * Handle update profile command
     */
    @Override
    @Transactional
    public void handle(UpdateProfileCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.updateProfile(
                command.firstName(),
                command.lastName(),
                command.phone(),
                command.timezoneId(),
                command.languageId()
        );

        userRepository.save(user);
    }

    /**
     * Handle save payment method command
     */
    @Override
    @Transactional
    public void handle(SavePaymentMethodCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Validate CVV (but don't store it)
        if (command.cvv() == null || command.cvv().length() != 3) {
            throw new ValidationException("Invalid CVV");
        }

        // Create payment method (only stores last 4 digits)
        PaymentMethod paymentMethod = PaymentMethod.fromCardNumber(
                command.cardholderName(),
                command.cardNumber(),
                command.cardType(),
                command.expiryMonth(),
                command.expiryYear()
        );

        user.setPaymentMethod(paymentMethod);
        userRepository.save(user);
    }

    /**
     * Handle update plan command
     */
    @Override
    @Transactional
    public void handle(UpdatePlanCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.updatePlan(command.newPlan());
        userRepository.save(user);
    }

    /**
     * Handle forgot password command
     */
    @Override
    @Transactional
    public void handle(ForgotPasswordCommand command) {
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Generate reset token (simplified - in production use a proper token store)
        String resetToken = UUID.randomUUID().toString();

        // Send reset email
        emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
    }

    /**
     * Handle update avatar command
     */
    @Override
    @Transactional
    public void handle(UpdateAvatarCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.updateAvatar(command.avatarUrl());
        userRepository.save(user);
    }
}