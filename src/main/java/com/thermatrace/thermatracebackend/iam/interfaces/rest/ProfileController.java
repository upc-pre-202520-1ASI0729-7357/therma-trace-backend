package com.thermatrace.thermatracebackend.iam.interfaces.rest;

import com.thermatrace.thermatracebackend.iam.domain.model.commands.UpdateProfileCommand;
import com.thermatrace.thermatracebackend.iam.domain.model.queries.GetUserByEmailQuery;
import com.thermatrace.thermatracebackend.iam.domain.services.UserCommandService;
import com.thermatrace.thermatracebackend.iam.domain.services.UserQueryService;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.ProfileResource;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.UpdateProfileResource;
import com.thermatrace.thermatracebackend.iam.interfaces.transform.ProfileResourceFromEntityAssembler;
import com.thermatrace.thermatracebackend.shared.domain.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * ProfileController
 * Handles user profile operations
 */
@RestController
@RequestMapping(value = "/api/v1/profile", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Profile", description = "User profile management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public ProfileController(UserQueryService userQueryService, UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    /**
     * Get current authenticated user's profile
     */
    @GetMapping
    @Operation(summary = "Get profile", description = "Get current authenticated user's profile")
    public ResponseEntity<ProfileResource> getProfile(Authentication authentication) {
        String email = authentication.getName(); // JWT subject is email

        var user = userQueryService.handle(new GetUserByEmailQuery(email))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(user);
        return ResponseEntity.ok(profileResource);
    }

    /**
     * Update current user's profile
     */
    @PatchMapping
    @Operation(summary = "Update profile", description = "Update current authenticated user's profile")
    public ResponseEntity<ProfileResource> updateProfile(
            @RequestBody UpdateProfileResource resource,
            Authentication authentication) {

        String email = authentication.getName();

        // Get user to get their ID
        var user = userQueryService.handle(new GetUserByEmailQuery(email))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Create command
        var command = new UpdateProfileCommand(
                user.getId(),
                null,  // firstName not updated here
                null,  // lastName not updated here
                resource.phone(),
                resource.timezoneId(),
                resource.languageId()
        );

        // Execute command
        userCommandService.handle(command);

        // Get updated user
        var updatedUser = userQueryService.handle(new GetUserByEmailQuery(email))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        var profileResource = ProfileResourceFromEntityAssembler.toResourceFromEntity(updatedUser);
        return ResponseEntity.ok(profileResource);
    }
}
