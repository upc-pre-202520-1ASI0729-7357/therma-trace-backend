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
        seedAdminUser();
        var medicines = seedMedicines();
        seedMonitoring(medicines);
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

    private void seedAdminUser() {
        final String adminEmail = "admin@thermatrace.com";
        if (userRepository.existsByEmail(adminEmail)) {
            log.info("Admin user already exists: {}", adminEmail);
            return;
        }
        var admin = new User("Admin", "ThermaTrace", adminEmail, hashingService.encode("ThermaTrace123!"));
        // language and timezone optional defaults
        admin.setLanguageId("es");
        admin.setTimezoneId("America/Lima");

        // assign roles
        var roleUser = roleRepository.findByName(Roles.ROLE_USER).orElseThrow();
        var roleAdmin = roleRepository.findByName(Roles.ROLE_ADMIN).orElseThrow();
        admin.addRole(roleUser);
        admin.addRole(roleAdmin);

        userRepository.save(admin);
        log.info("Seeded admin user: {}", adminEmail);
    }

    private List<Medicine> seedMedicines() {
        if (medicineRepository.count() > 0) {
            return medicineRepository.findAll();
        }
        var m1 = new Medicine("Penicilina", LocalDate.parse("2025-10-15"), "https://example.com/images/penicilina.jpg");
        var m2 = new Medicine("Ibuprofeno", LocalDate.now().plusMonths(8), "https://example.com/images/ibuprofeno.jpg");
        var m3 = new Medicine("Paracetamol", LocalDate.now().plusMonths(12), "https://example.com/images/paracetamol.jpg");
        var saved = medicineRepository.saveAll(List.of(m1, m2, m3));
        log.info("Seeded {} medicines", saved.size());
        return saved;
    }

    private void seedMonitoring(List<Medicine> medicines) {
        if (medicines == null || medicines.isEmpty()) return;
        if (monitoringRepository.count() > 0) return;

        // create 2-3 monitoring rows
        var first = medicines.get(0);
        var mon1 = new Monitoring(first.getId(), 0.3, "active", 2, "block A");

        var second = medicines.size() > 1 ? medicines.get(1) : first;
        var mon2 = new Monitoring(second.getId(), 4.2, "active", 10, "Principal storage");

        var third = medicines.size() > 2 ? medicines.get(2) : first;
        var mon3 = new Monitoring(third.getId(), 7.0, "inactive", 0, "Block B");

        monitoringRepository.saveAll(List.of(mon1, mon2, mon3));
        log.info("Seeded monitoring entries: {}", 3);
    }
}
