package com.thermatrace.thermatracebackend.monitoring.interfaces.rest;

import com.thermatrace.thermatracebackend.iam.domain.model.queries.GetUserByEmailQuery;
import com.thermatrace.thermatracebackend.iam.domain.services.UserQueryService;
import com.thermatrace.thermatracebackend.monitoring.domain.model.commands.DeleteMonitoringCommand;
import com.thermatrace.thermatracebackend.monitoring.domain.model.commands.UpdateMonitoringCommand;
import com.thermatrace.thermatracebackend.monitoring.domain.model.queries.GetAllMonitoringsByUserIdQuery;
import com.thermatrace.thermatracebackend.monitoring.domain.model.queries.GetMonitoringByIdAndUserIdQuery;
import com.thermatrace.thermatracebackend.monitoring.domain.services.MonitoringCommandService;
import com.thermatrace.thermatracebackend.monitoring.domain.services.MonitoringQueryService;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources.CreateMonitoringResource;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources.MonitoringResource;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources.UpdateMonitoringResource;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.transform.CreateMonitoringCommandFromResourceAssembler;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.transform.MonitoringResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/medicine-monitoring", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Medicine Monitoring", description = "Endpoints for medicine monitoring")
@SecurityRequirement(name = "bearerAuth")
public class MedicineMonitoringController {
    private final MonitoringCommandService monitoringCommandService;
    private final MonitoringQueryService monitoringQueryService;
    private final UserQueryService userQueryService;
    private final CreateMonitoringCommandFromResourceAssembler createAssembler;
    private final MonitoringResourceFromEntityAssembler resourceAssembler;

    public MedicineMonitoringController(MonitoringCommandService monitoringCommandService,
                                       MonitoringQueryService monitoringQueryService,
                                       UserQueryService userQueryService,
                                       CreateMonitoringCommandFromResourceAssembler createAssembler,
                                       MonitoringResourceFromEntityAssembler resourceAssembler) {
        this.monitoringCommandService = monitoringCommandService;
        this.monitoringQueryService = monitoringQueryService;
        this.userQueryService = userQueryService;
        this.createAssembler = createAssembler;
        this.resourceAssembler = resourceAssembler;
    }

    /**
     * Helper method to get userId from JWT authentication
     */
    private Long getUserIdFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return userQueryService.handle(new GetUserByEmailQuery(email))
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    @Operation(summary = "Create a monitoring record", description = "Creates a new monitoring entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Monitoring entry created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid monitoring data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitoringResource> createMonitoring(
            @RequestBody CreateMonitoringResource resource,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        var command = createAssembler.toCommand(userId, resource);
        var result = monitoringCommandService.handle(command);
        return result.map(monitoring ->
                new ResponseEntity<>(resourceAssembler.toResource(monitoring), HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @Operation(summary = "Get all monitoring records for authenticated user", description = "Retrieves all monitoring entries belonging to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monitoring entries retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MonitoringResource>> getAllMonitorings(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        var monitorings = monitoringQueryService.handle(new GetAllMonitoringsByUserIdQuery(userId));
        var monitoringResources = monitorings.stream()
                .map(resourceAssembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(monitoringResources);
    }

    @Operation(summary = "Get monitoring by ID", description = "Retrieves a monitoring entry by its id for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monitoring entry retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Monitoring entry not found or does not belong to user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitoringResource> getMonitoringById(
            @Parameter(description = "Monitoring ID", required = true)
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        var monitoring = monitoringQueryService.handle(new GetMonitoringByIdAndUserIdQuery(id, userId));

        if (monitoring.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var monitoringResource = resourceAssembler.toResource(monitoring.get());
        return ResponseEntity.ok(monitoringResource);
    }

    @Operation(summary = "Update a monitoring record", description = "Updates an existing monitoring entry belonging to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Monitoring entry updated successfully"),
            @ApiResponse(responseCode = "404", description = "Monitoring entry not found or does not belong to user"),
            @ApiResponse(responseCode = "400", description = "Invalid monitoring data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitoringResource> updateMonitoring(
            @Parameter(description = "Monitoring ID", required = true)
            @PathVariable Long id,
            @RequestBody UpdateMonitoringResource resource,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        var command = new UpdateMonitoringCommand(
            userId,
            id,
            resource.medicineId(),
            resource.temperature(),
            resource.state(),
            resource.stock(),
            resource.location()
        );
        var result = monitoringCommandService.handle(command);
        return result.map(monitoring ->
                ResponseEntity.ok(resourceAssembler.toResource(monitoring)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a monitoring record", description = "Deletes a monitoring entry by id if it belongs to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Monitoring entry deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Monitoring entry not found or does not belong to user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteMonitoring(
            @Parameter(description = "Monitoring ID", required = true)
            @PathVariable Long id,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        // First verify the monitoring entry belongs to the user
        var monitoring = monitoringQueryService.handle(new GetMonitoringByIdAndUserIdQuery(id, userId));

        if (monitoring.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            monitoringCommandService.handle(new DeleteMonitoringCommand(id));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
