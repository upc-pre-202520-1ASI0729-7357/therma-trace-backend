package com.thermatrace.thermatracebackend.iam.interfaces.rest;

import com.thermatrace.thermatracebackend.iam.domain.model.aggregates.User;
import com.thermatrace.thermatracebackend.iam.domain.model.commands.SignInCommand;
import com.thermatrace.thermatracebackend.iam.domain.model.commands.SignUpCommand;
import com.thermatrace.thermatracebackend.iam.domain.model.queries.GetUserByIdQuery;
import com.thermatrace.thermatracebackend.iam.domain.services.UserCommandService;
import com.thermatrace.thermatracebackend.iam.domain.services.UserQueryService;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.SignInResource;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.SignUpResource;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.UserResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthenticationController
 * Handles authentication requests (sign-up and sign-in)
 */
@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication endpoints for user registration and login")
public class AuthenticationController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public AuthenticationController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    /**
     * Handles the sign-up request.
     * @param signUpResource the sign-up request body
     * @return the created user resource
     */
    @PostMapping("/sign-up")
    @Operation(summary = "Sign-up", description = "Register a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully."),
            @ApiResponse(responseCode = "400", description = "Bad request - Email already exists or validation error.")
    })
    public ResponseEntity<UserResource> signUp(@RequestBody SignUpResource signUpResource) {
        var signUpCommand = new SignUpCommand(
                signUpResource.firstName(),
                signUpResource.lastName(),
                signUpResource.email(),
                signUpResource.password(),
                signUpResource.roles()
        );

        Long userId = userCommandService.handle(signUpCommand);

        User user = userQueryService.handle(new GetUserByIdQuery(userId))
                .orElseThrow(() -> new RuntimeException("User not found after creation"));

        var userResource = new UserResource(
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

        return new ResponseEntity<>(userResource, HttpStatus.CREATED);
    }

    /**
     * Handles the sign-in request.
     * @param signInResource the sign-in request body
     * @return the authenticated user resource with JWT token
     */
    @PostMapping("/sign-in")
    @Operation(summary = "Sign-in", description = "Authenticate user and receive JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully."),
            @ApiResponse(responseCode = "404", description = "User not found or invalid credentials.")
    })
    public ResponseEntity<AuthenticatedUserResource> signIn(@RequestBody SignInResource signInResource) {
        var signInCommand = new SignInCommand(signInResource.email(), signInResource.password());

        String token = userCommandService.handle(signInCommand)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        User user = userQueryService.handle(new com.thermatrace.thermatracebackend.iam.domain.model.queries.GetUserByEmailQuery(signInResource.email()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        var authenticatedUserResource = new AuthenticatedUserResource(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                token
        );

        return ResponseEntity.ok(authenticatedUserResource);
    }
}