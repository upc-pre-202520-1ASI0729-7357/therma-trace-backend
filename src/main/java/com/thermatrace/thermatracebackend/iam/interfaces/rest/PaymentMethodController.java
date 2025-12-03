package com.thermatrace.thermatracebackend.iam.interfaces.rest;

import com.thermatrace.thermatracebackend.iam.domain.model.commands.DeletePaymentMethodCommand;
import com.thermatrace.thermatracebackend.iam.domain.model.queries.GetPaymentMethodByUserIdQuery;
import com.thermatrace.thermatracebackend.iam.domain.model.queries.GetUserByEmailQuery;
import com.thermatrace.thermatracebackend.iam.domain.services.PaymentMethodCommandService;
import com.thermatrace.thermatracebackend.iam.domain.services.PaymentMethodQueryService;
import com.thermatrace.thermatracebackend.iam.domain.services.UserQueryService;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.CreatePaymentMethodResource;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.PaymentMethodResource;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.resources.UpdatePaymentMethodResource;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.transform.CreatePaymentMethodCommandFromResourceAssembler;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.transform.PaymentMethodResourceFromEntityAssembler;
import com.thermatrace.thermatracebackend.iam.interfaces.rest.transform.UpdatePaymentMethodCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/paymentMethods", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Payment Methods", description = "Endpoints for managing user payment methods")
@SecurityRequirement(name = "bearerAuth")
public class PaymentMethodController {

    private static final Logger log = LoggerFactory.getLogger(PaymentMethodController.class);

    private final PaymentMethodCommandService paymentMethodCommandService;
    private final PaymentMethodQueryService paymentMethodQueryService;
    private final UserQueryService userQueryService;

    public PaymentMethodController(PaymentMethodCommandService paymentMethodCommandService,
                                   PaymentMethodQueryService paymentMethodQueryService,
                                   UserQueryService userQueryService) {
        this.paymentMethodCommandService = paymentMethodCommandService;
        this.paymentMethodQueryService = paymentMethodQueryService;
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

    @Operation(summary = "Get payment method", description = "Retrieves the authenticated user's payment method")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment method retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No payment method found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public ResponseEntity<PaymentMethodResource> getPaymentMethod(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        var paymentMethod = paymentMethodQueryService.handle(new GetPaymentMethodByUserIdQuery(userId));

        return paymentMethod
                .map(PaymentMethodResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Add payment method", description = "Adds a payment method for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment method added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment method data or user already has a payment method"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentMethodResource> addPaymentMethod(
            @RequestBody CreatePaymentMethodResource resource,
            Authentication authentication) {
        try {
            log.info("Received payment method creation request: cardholderName={}, cardNumber={}, expiryMonth={}, expiryYear={}, cvv={}",
                    resource.cardholderName(),
                    resource.cardNumber() != null ? "****" + (resource.cardNumber().length() > 4 ? resource.cardNumber().substring(resource.cardNumber().length() - 4) : "****") : "null",
                    resource.expiryMonth(),
                    resource.expiryYear(),
                    resource.cvv() != null ? "***" : "null");

            Long userId = getUserIdFromAuthentication(authentication);
            var command = CreatePaymentMethodCommandFromResourceAssembler.toCommandFromResource(userId, resource);
            var paymentMethod = paymentMethodCommandService.handle(command);

            return paymentMethod
                    .map(PaymentMethodResourceFromEntityAssembler::toResourceFromEntity)
                    .map(pm -> new ResponseEntity<>(pm, HttpStatus.CREATED))
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException | IllegalStateException e) {
            log.error("Error creating payment method: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Update payment method", description = "Updates the authenticated user's payment method")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment method updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment method data or no payment method found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentMethodResource> updatePaymentMethod(
            @RequestBody UpdatePaymentMethodResource resource,
            Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            var command = UpdatePaymentMethodCommandFromResourceAssembler.toCommandFromResource(userId, resource);
            var paymentMethod = paymentMethodCommandService.handle(command);

            return paymentMethod
                    .map(PaymentMethodResourceFromEntityAssembler::toResourceFromEntity)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete payment method", description = "Deletes the authenticated user's payment method")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Payment method deleted successfully"),
            @ApiResponse(responseCode = "400", description = "No payment method found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @DeleteMapping
    public ResponseEntity<Void> deletePaymentMethod(Authentication authentication) {
        try {
            Long userId = getUserIdFromAuthentication(authentication);
            paymentMethodCommandService.handle(new DeletePaymentMethodCommand(userId));
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}