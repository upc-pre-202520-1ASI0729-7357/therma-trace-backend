package com.thermatrace.thermatracebackend.medicines.interfaces.rest;

import com.thermatrace.thermatracebackend.medicines.domain.model.commands.DeleteMedicineCommand;
import com.thermatrace.thermatracebackend.medicines.domain.model.queries.GetAllMedicinesQuery;
import com.thermatrace.thermatracebackend.medicines.domain.model.queries.GetMedicineByIdQuery;
import com.thermatrace.thermatracebackend.medicines.domain.services.MedicineCommandService;
import com.thermatrace.thermatracebackend.medicines.domain.services.MedicineQueryService;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.resources.CreateMedicineResource;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.resources.MedicineResource;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.resources.UpdateMedicineResource;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.transform.CreateMedicineCommandFromResourceAssembler;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.transform.MedicineResourceFromEntityAssembler;
import com.thermatrace.thermatracebackend.medicines.interfaces.rest.transform.UpdateMedicineCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public MedicineController(MedicineCommandService medicineCommandService,
                             MedicineQueryService medicineQueryService) {
        this.medicineCommandService = medicineCommandService;
        this.medicineQueryService = medicineQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new medicine", description = "Creates a new medicine with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medicine created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid medicine data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<MedicineResource> createMedicine(@RequestBody CreateMedicineResource resource) {
        var createMedicineCommand = CreateMedicineCommandFromResourceAssembler.toCommandFromResource(resource);
        var medicine = medicineCommandService.handle(createMedicineCommand);

        if (medicine.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        var medicineResource = MedicineResourceFromEntityAssembler.toResourceFromEntity(medicine.get());
        return new ResponseEntity<>(medicineResource, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all medicines", description = "Retrieves all medicines")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicines retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<MedicineResource>> getAllMedicines() {
        var medicines = medicineQueryService.handle(new GetAllMedicinesQuery());
        var medicineResources = medicines.stream()
                .map(MedicineResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(medicineResources);
    }

    @GetMapping("/{medicineId}")
    @Operation(summary = "Get medicine by ID", description = "Retrieves a medicine by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Medicine not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<MedicineResource> getMedicineById(
            @Parameter(description = "Medicine ID", required = true)
            @PathVariable Long medicineId) {
        var medicine = medicineQueryService.handle(new GetMedicineByIdQuery(medicineId));

        if (medicine.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var medicineResource = MedicineResourceFromEntityAssembler.toResourceFromEntity(medicine.get());
        return ResponseEntity.ok(medicineResource);
    }

    @PutMapping("/{medicineId}")
    @Operation(summary = "Update medicine", description = "Updates an existing medicine")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Medicine updated successfully"),
            @ApiResponse(responseCode = "404", description = "Medicine not found"),
            @ApiResponse(responseCode = "400", description = "Invalid medicine data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<MedicineResource> updateMedicine(
            @Parameter(description = "Medicine ID", required = true)
            @PathVariable Long medicineId,
            @RequestBody UpdateMedicineResource resource) {
        var updateMedicineCommand = UpdateMedicineCommandFromResourceAssembler.toCommandFromResource(medicineId, resource);
        var medicine = medicineCommandService.handle(updateMedicineCommand);

        if (medicine.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var medicineResource = MedicineResourceFromEntityAssembler.toResourceFromEntity(medicine.get());
        return ResponseEntity.ok(medicineResource);
    }

    @DeleteMapping("/{medicineId}")
    @Operation(summary = "Delete medicine", description = "Deletes a medicine by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Medicine deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Medicine not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<Void> deleteMedicine(
            @Parameter(description = "Medicine ID", required = true)
            @PathVariable Long medicineId) {
        try {
            medicineCommandService.handle(new DeleteMedicineCommand(medicineId));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
