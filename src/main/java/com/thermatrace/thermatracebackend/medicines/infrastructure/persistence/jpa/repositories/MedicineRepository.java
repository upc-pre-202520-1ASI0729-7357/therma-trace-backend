package com.thermatrace.thermatracebackend.medicines.infrastructure.persistence.jpa.repositories;

import com.thermatrace.thermatracebackend.medicines.domain.model.aggregates.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
}
