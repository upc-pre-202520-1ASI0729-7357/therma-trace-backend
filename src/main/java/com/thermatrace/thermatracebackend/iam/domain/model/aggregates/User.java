package com.thermatrace.thermatracebackend.iam.domain.model.aggregates;

import com.thermatrace.thermatracebackend.iam.domain.model.entities.PaymentMethod;
import com.thermatrace.thermatracebackend.iam.domain.model.entities.Role;
import com.thermatrace.thermatracebackend.iam.domain.model.valueobjects.Plans;
import com.thermatrace.thermatracebackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AuditableAbstractAggregateRoot<User> {

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false)
    private String password;

    @Size(max = 20)
    private String phone;

    @Column(length = 512)
    private String avatar; // URL to avatar image

    @Column(length = 50)
    private String timezoneId; // e.g., "America/New_York"

    @Column(length = 10)
    private String languageId; // e.g., "en", "es"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Plans currentPlan = Plans.FREEMIUM;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;

    // Default constructor
    public User() {
        this.currentPlan = Plans.FREEMIUM;
    }

    // Constructor for registration
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.currentPlan = Plans.FREEMIUM;
    }

    // Business logic methods

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }

    public void updatePlan(Plans newPlan) {
        this.currentPlan = newPlan;
    }

    public void updateProfile(String firstName, String lastName, String phone, String timezoneId, String languageId) {
        if (firstName != null && !firstName.isBlank()) {
            this.firstName = firstName;
        }
        if (lastName != null && !lastName.isBlank()) {
            this.lastName = lastName;
        }
        if (phone != null) {
            this.phone = phone;
        }
        if (timezoneId != null) {
            this.timezoneId = timezoneId;
        }
        if (languageId != null) {
            this.languageId = languageId;
        }
    }

    public void updateAvatar(String avatarUrl) {
        this.avatar = avatarUrl;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean canAddMedicament(int currentMedicamentCount) {
        return this.currentPlan.canAddMedicament(currentMedicamentCount);
    }

    public int getMedicamentLimit() {
        return this.currentPlan.getMedicamentLimit();
    }

    public boolean hasDailyAlerts() {
        return this.currentPlan.hasDailyAlerts();
    }

    public boolean hasAdvancedReports() {
        return this.currentPlan.hasAdvancedReports();
    }
}