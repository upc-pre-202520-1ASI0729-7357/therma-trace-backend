package com.thermatrace.thermatracebackend.medicines.interfaces.rest;

import com.thermatrace.thermatracebackend.iam.domain.model.queries.GetUserByEmailQuery;
import com.thermatrace.thermatracebackend.iam.domain.services.UserQueryService;
import com.thermatrace.thermatracebackend.medicines.domain.model.commands.DeleteMedicineCommand;
import com.thermatrace.thermatracebackend.medicines.domain.model.queries.GetAllMedicinesByUserIdQuery;
import com.thermatrace.thermatracebackend.medicines.domain.model.queries.GetMedicineByIdAndUserIdQuery;
import com.thermatrace.thermatracebackend.medicines.domain.services.MedicineCommandService;
import com.thermatrace.thermatracebackend.medicines.domain.services.MedicineQueryService;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.resources.CreateMedicineResource;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.resources.MedicineResource;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.resources.UpdateMedicineResource;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.transform.CreateMedicineCommandFromResourceAssembler;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.transform.MedicineResourceFromEntityAssembler;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.transform.UpdateMedicineCommandFromResourceAssembler;
import com.thermatrace.thermatracebackend.shared.interfaces.rest.resources.MessageResource;
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

/**
 * MedicineController
 * Handles medicine management operations
 */
@RestController
@RequestMapping(value = "/api/v1/medicines", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Medicines", description = "Medicine management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class MedicineController {

    private final MedicineCommandService medicineCommandService;
    private final MedicineQueryService medicineQueryService;
    private final UserQueryService userQueryService;

    public MedicineController(MedicineCommandService medicineCommandService,
                             MedicineQueryService medicineQueryService,
                             UserQueryService userQueryService) {
        this.medicineCommandService = medicineCommandService;
        this.medicineQueryService = medicineQueryService;
        this.userQueryService = userQueryService;
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

    @PostMapping
    @Operation(summary = "Create a new medicine", description = "Creates a new medicine with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medicine created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid medicine data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<MedicineResource> createMedicine(
            @RequestBody CreateMedicineResource resource,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        var createMedicineCommand = CreateMedicineCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var medicine = medicineCommandService.handle(createMedicineCommand);

        if (medicine.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var medicineResource = MedicineResourceFromEntityAssembler.toResourceFromEntity(medicine.get());
        return new ResponseEntity<>(medicineResource, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all medicines for authenticated user", description = "Retrieves all medicines belonging to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicines retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<MedicineResource>> getAllMedicines(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        var medicines = medicineQueryService.handle(new GetAllMedicinesByUserIdQuery(userId));
        var medicineResources = medicines.stream()
                .map(MedicineResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(medicineResources);
    }

    @GetMapping("/{medicineId}")
    @Operation(summary = "Get medicine by ID", description = "Retrieves a medicine by its unique identifier for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Medicine not found or does not belong to user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<MedicineResource> getMedicineById(
            @Parameter(description = "Medicine ID", required = true)
            @PathVariable Long medicineId,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        var medicine = medicineQueryService.handle(new GetMedicineByIdAndUserIdQuery(medicineId, userId));

        if (medicine.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var medicineResource = MedicineResourceFromEntityAssembler.toResourceFromEntity(medicine.get());
        return ResponseEntity.ok(medicineResource);
    }

    @PutMapping("/{medicineId}")
    @Operation(summary = "Update medicine", description = "Updates an existing medicine belonging to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine updated successfully"),
            @ApiResponse(responseCode = "404", description = "Medicine not found or does not belong to user"),
            @ApiResponse(responseCode = "400", description = "Invalid medicine data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<MedicineResource> updateMedicine(
            @Parameter(description = "Medicine ID", required = true)
            @PathVariable Long medicineId,
            @RequestBody UpdateMedicineResource resource,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        var updateMedicineCommand = UpdateMedicineCommandFromResourceAssembler.toCommandFromResource(userId, medicineId, resource);
        var medicine = medicineCommandService.handle(updateMedicineCommand);

        if (medicine.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var medicineResource = MedicineResourceFromEntityAssembler.toResourceFromEntity(medicine.get());
        return ResponseEntity.ok(medicineResource);
    }

    @DeleteMapping("/{medicineId}")
    @Operation(summary = "Delete medicine", description = "Deletes a medicine by its unique identifier if it belongs to the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medicine deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Medicine not found or does not belong to user"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteMedicine(
            @Parameter(description = "Medicine ID", required = true)
            @PathVariable Long medicineId,
            Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        // First verify the medicine belongs to the user
        var medicine = medicineQueryService.handle(new GetMedicineByIdAndUserIdQuery(medicineId, userId));

        if (medicine.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            medicineCommandService.handle(new DeleteMedicineCommand(medicineId));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
