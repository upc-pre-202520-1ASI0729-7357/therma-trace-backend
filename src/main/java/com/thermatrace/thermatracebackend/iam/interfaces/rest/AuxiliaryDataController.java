package com.thermatrace.thermatracebackend.iam.interfaces.rest;

import com.thermatrace.thermatracebackend.iam.domain.model.valueobjects.Plans;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.LanguageResource;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.PlanResource;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.TimezoneResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AuxiliaryDataController
 * Provides auxiliary data like timezones, plans, and languages
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Auxiliary Data", description = "Endpoints for auxiliary data (timezones, plans, languages)")
public class AuxiliaryDataController {

    /**
     * Get all available timezones
     */
    @GetMapping("/timezones")
    @Operation(summary = "Get timezones", description = "Get list of available timezones")
    public ResponseEntity<List<TimezoneResource>> getTimezones() {
        List<TimezoneResource> timezones = ZoneId.getAvailableZoneIds()
                .stream()
                .sorted()
                .map(zoneId -> {
                    try {
                        ZoneId zone = ZoneId.of(zoneId);
                        ZoneOffset offset = zone.getRules().getOffset(Instant.now());
                        String offsetStr = formatOffset(offset);

                        return new TimezoneResource(
                                zoneId,
                                zoneId.replace("_", " "),
                                offsetStr
                        );
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(tz -> tz != null)
                .collect(Collectors.toList());

        return ResponseEntity.ok(timezones);
    }

    /**
     * Get all subscription plans
     */
    @GetMapping("/plans")
    @Operation(summary = "Get plans", description = "Get list of available subscription plans")
    public ResponseEntity<List<PlanResource>> getPlans() {
        List<PlanResource> plans = Arrays.stream(Plans.values())
                .map(plan -> new PlanResource(
                        plan.name(),
                        plan.getDisplayName(),
                        plan.getPrice(),
                        plan.getFeatures()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(plans);
    }

    /**
     * Get all supported languages
     */
    @GetMapping("/languages")
    @Operation(summary = "Get languages", description = "Get list of supported languages")
    public ResponseEntity<List<LanguageResource>> getLanguages() {
        List<LanguageResource> languages = List.of(
                new LanguageResource("en", "English", "en"),
                new LanguageResource("es", "Español", "es"),
                new LanguageResource("fr", "Français", "fr"),
                new LanguageResource("de", "Deutsch", "de"),
                new LanguageResource("pt", "Português", "pt")
        );

        return ResponseEntity.ok(languages);
    }

    /**
     * Format ZoneOffset to readable string
     */
    private String formatOffset(ZoneOffset offset) {
        int totalSeconds = offset.getTotalSeconds();
        int hours = totalSeconds / 3600;
        int minutes = Math.abs((totalSeconds % 3600) / 60);

        String sign = hours >= 0 ? "+" : "";
        return String.format("UTC%s%02d:%02d", sign, hours, minutes);
    }
}
