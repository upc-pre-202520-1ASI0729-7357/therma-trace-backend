package com.thermatrace.thermatracebackend.shared.infrastructure.bootstrap;

import com.thermatrace.thermatracebackend.iam.application.internal.outboundservices.HashingService;
import com.thermatrace.thermatracebackend.iam.domain.model.aggregates.User;
import com.thermatrace.thermatracebackend.iam.domain.model.entities.Role;
import com.thermatrace.thermatracebackend.iam.domain.model.valueobjects.Roles;
import com.thermatrace.thermatracebackend.iam.infrastructure.persistence.jpa.RoleRepository;
import com.thermatrace.thermatracebackend.iam.infrastructure.persistence.jpa.UserRepository;
import com.thermatrace.thermatracebackend.medicines.domain.model.aggregates.Medicine;
import com.thermatrace.thermatracebackend.medicines.infrastructure.persistence.jpa.repositories.MedicineRepository;
import com.thermatrace.thermatracebackend.monitoring.domain.model.aggregates.Monitoring;
import com.thermatrace.thermatracebackend.monitoring.infrastructure.persistence.jpa.repositories.MonitoringRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Order(10)
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final MedicineRepository medicineRepository;
    private final MonitoringRepository monitoringRepository;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           HashingService hashingService,
                           MedicineRepository medicineRepository,
                           MonitoringRepository monitoringRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.medicineRepository = medicineRepository;
        this.monitoringRepository = monitoringRepository;
    }

    @Override
    public void run(String... args) {
        seedRoles();
        var adminUser = seedAdminUser();
        var regularUser = seedRegularUser();

        // Seed medicines for admin user
        var adminMedicines = seedMedicines(adminUser.getId(), "admin");
        seedMonitoring(adminUser.getId(), adminMedicines, "admin");

        // Seed medicines for regular user
        var regularMedicines = seedMedicines(regularUser.getId(), "regular");
        seedMonitoring(regularUser.getId(), regularMedicines, "regular");

        log.info("Data seeding completed");
    }

    private void seedRoles() {
        if (!roleRepository.existsByName(Roles.ROLE_USER)) {
            roleRepository.save(new Role(Roles.ROLE_USER));
            log.info("Seeded ROLE_USER");
        }
        if (!roleRepository.existsByName(Roles.ROLE_ADMIN)) {
            roleRepository.save(new Role(Roles.ROLE_ADMIN));
            log.info("Seeded ROLE_ADMIN");
        }
    }

    private User seedAdminUser() {
        final String adminEmail = "admin@thermatrace.com";
        if (userRepository.existsByEmail(adminEmail)) {
            var existingAdmin = userRepository.findByEmail(adminEmail).orElseThrow();
            log.info("Admin user already exists: {}", adminEmail);
            return existingAdmin;
        }
        var admin = new User("Admin", "ThermaTrace", adminEmail, hashingService.encode("ThermaTrace123!"));
        admin.setLanguageId("es");
        admin.setTimezoneId("America/Lima");

        // Admin gets ROLE_ADMIN only
        var roleAdmin = roleRepository.findByName(Roles.ROLE_ADMIN).orElseThrow();
        admin.addRole(roleAdmin);

        var savedAdmin = userRepository.save(admin);
        log.info("Seeded admin user: {} with ROLE_ADMIN", adminEmail);
        return savedAdmin;
    }

    private User seedRegularUser() {
        final String userEmail = "user@thermatrace.com";
        if (userRepository.existsByEmail(userEmail)) {
            var existingUser = userRepository.findByEmail(userEmail).orElseThrow();
            log.info("Regular user already exists: {}", userEmail);
            return existingUser;
        }
        var user = new User("John", "Doe", userEmail, hashingService.encode("User123"));
        user.setLanguageId("en");
        user.setTimezoneId("America/New_York");

        // Regular user gets ROLE_USER only
        var roleUser = roleRepository.findByName(Roles.ROLE_USER).orElseThrow();
        user.addRole(roleUser);

        var savedUser = userRepository.save(user);
        log.info("Seeded regular user: {} with ROLE_USER", userEmail);
        return savedUser;
    }

    private List<Medicine> seedMedicines(Long userId, String userType) {
        // Check if medicines already exist for this user
        var existingMedicines = medicineRepository.findAllByUserId(userId);
        if (!existingMedicines.isEmpty()) {
            log.info("Medicines already exist for {} user (ID: {}), found {} medicines", userType, userId, existingMedicines.size());
            return existingMedicines;
        }

        var m1 = new Medicine(userId, "Penicillin", LocalDate.parse("2025-10-15"), "https://example.com/images/penicillin.jpg");
        var m2 = new Medicine(userId, "Ibuprofen", LocalDate.now().plusMonths(8), "https://example.com/images/ibuprofen.jpg");
        var m3 = new Medicine(userId, "Acetaminophen", LocalDate.now().plusMonths(12), "https://example.com/images/acetaminophen.jpg");
        var saved = medicineRepository.saveAll(List.of(m1, m2, m3));
        log.info("Seeded {} medicines for {} user (ID: {})", saved.size(), userType, userId);
        return saved;
    }

    private void seedMonitoring(Long userId, List<Medicine> medicines, String userType) {
        if (medicines == null || medicines.isEmpty()) return;

        // Check if monitoring entries already exist for this user
        var existingMonitoring = monitoringRepository.findAllByUserId(userId);
        if (!existingMonitoring.isEmpty()) {
            log.info("Monitoring entries already exist for {} user (ID: {}), found {} entries", userType, userId, existingMonitoring.size());
            return;
        }

        // create 3 monitoring entries
        var first = medicines.get(0);
        var mon1 = new Monitoring(userId, first.getId(), 0.5, "active", 2, "Block A");

        var second = medicines.size() > 1 ? medicines.get(1) : first;
        var mon2 = new Monitoring(userId, second.getId(), 4.2, "active", 10, "Main Storage");

        var third = medicines.size() > 2 ? medicines.get(2) : first;
        var mon3 = new Monitoring(userId, third.getId(), 8.5, "inactive", 0, "Block B");

        monitoringRepository.saveAll(List.of(mon1, mon2, mon3));
        log.info("Seeded {} monitoring entries for {} user (ID: {})", 3, userType, userId);
    }
}
