package com.thermatrace.thermatracebackend.medicines.infrastructure.persistence.jpa.repositories;

import com.thermatrace.thermatracebackend.medicines.domain.model.aggregates.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {
    List<Medicine> findAllByUserId(Long userId);
    Optional<Medicine> findByIdAndUserId(Long id, Long userId);
}
