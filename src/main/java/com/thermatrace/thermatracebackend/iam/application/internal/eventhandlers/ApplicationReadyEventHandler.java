package com.thermatrace.thermatracebackend.iam.application.internal.eventhandlers;

import com.thermatrace.thermatracebackend.iam.domain.model.entities.Role;
import com.thermatrace.thermatracebackend.iam.domain.model.valueobjects.Roles;
import com.thermatrace.thermatracebackend.iam.infrastructure.persistence.jpa.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * ApplicationReadyEventHandler
 * This class handles the ApplicationReadyEvent and seeds the database with the default roles.
 */
@Component
public class ApplicationReadyEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationReadyEventHandler.class);
    private final RoleRepository roleRepository;

    public ApplicationReadyEventHandler(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void on(ApplicationReadyEvent event) {
        LOGGER.info("ApplicationReadyEvent received. Seeding default roles...");

        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(role));
                LOGGER.info("Role {} seeded", role.name());
            } else {
                LOGGER.info("Role {} already exists", role.name());
            }
        });

        LOGGER.info("Role seeding completed");
    }
}