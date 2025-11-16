package com.thermatrace.thermatracebackend.monitoring.interfaces.rest;

import com.thermatrace.thermatracebackend.monitoring.domain.services.MonitoringCommandService;
import com.thermatrace.thermatracebackend.monitoring.domain.services.MonitoringQueryService;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources.CreateMonitoringResource;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources.MonitoringResource;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.resources.UpdateMonitoringResource;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.transform.CreateMonitoringCommandFromResourceAssembler;
import com.thermatrace.thermatracebackend.monitoring.interfaces.rest.transform.MonitoringResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.thermatrace.thermatracebackend.monitoring.domain.model.commands.UpdateMonitoringCommand;
import com.thermatrace.thermatracebackend.monitoring.domain.model.commands.DeleteMonitoringCommand;

@RestController
@RequestMapping(value = "/api/v1/medicine-monitoring", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Medicine Monitoring", description = "Endpoints for medicine monitoring")
public class MedicineMonitoringController {
    private final MonitoringCommandService monitoringCommandService;
    private final MonitoringQueryService monitoringQueryService;
    private final CreateMonitoringCommandFromResourceAssembler createAssembler;
    private final MonitoringResourceFromEntityAssembler resourceAssembler;

    public MedicineMonitoringController(MonitoringCommandService monitoringCommandService,
                                       MonitoringQueryService monitoringQueryService,
                                       CreateMonitoringCommandFromResourceAssembler createAssembler,
                                       MonitoringResourceFromEntityAssembler resourceAssembler) {
        this.monitoringCommandService = monitoringCommandService;
        this.monitoringQueryService = monitoringQueryService;
        this.createAssembler = createAssembler;
        this.resourceAssembler = resourceAssembler;
    }

    @Operation(summary = "Create a monitoring record", description = "Creates a new monitoring entry")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitoringResource> createMonitoring(@RequestBody CreateMonitoringResource resource) {
        var command = createAssembler.toCommand(resource);
        var result = monitoringCommandService.handle(command);
        return result.map(monitoring ->
                new ResponseEntity<>(resourceAssembler.toResource(monitoring), HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @Operation(summary = "Get all monitoring records", description = "Retrieves all monitoring entries")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MonitoringResource> getAllMonitorings() {
        return monitoringQueryService.getAll().stream()
                .map(resourceAssembler::toResource)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get monitoring by ID", description = "Retrieves a monitoring entry by its id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitoringResource> getMonitoringById(@PathVariable Long id) {
        return monitoringQueryService.getById(id)
                .map(resourceAssembler::toResource)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update a monitoring record", description = "Updates an existing monitoring entry")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonitoringResource> updateMonitoring(@PathVariable Long id, @RequestBody UpdateMonitoringResource resource) {
        var command = new UpdateMonitoringCommand(
            id,
            resource.medicineId(),
            resource.medicineName(),
            resource.temperatura(),
            resource.estado(),
            resource.stock(),
            resource.ubicacion()
        );
        var result = monitoringCommandService.handle(command);
        return result.map(monitoring ->
                ResponseEntity.ok(resourceAssembler.toResource(monitoring)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a monitoring record", description = "Deletes a monitoring entry by id")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteMonitoring(@PathVariable Long id) {
        monitoringCommandService.handle(new DeleteMonitoringCommand(id));
        return ResponseEntity.noContent().build();
    }
}
