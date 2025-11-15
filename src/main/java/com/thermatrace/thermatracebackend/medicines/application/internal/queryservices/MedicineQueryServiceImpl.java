package com.thermatrace.thermatracebackend.medicines.application.internal.queryservices;

import com.thermatrace.thermatracebackend.medicines.domain.model.aggregates.Medicine;
import com.thermatrace.thermatracebackend.medicines.domain.model.queries.GetAllMedicinesQuery;
import com.thermatrace.thermatracebackend.medicines.domain.model.queries.GetMedicineByIdQuery;
import com.thermatrace.thermatracebackend.medicines.domain.services.MedicineQueryService;
import com.thermatrace.thermatracebackend.medicines.infrastructure.persistence.jpa.repositories.MedicineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineQueryServiceImpl implements MedicineQueryService {

    private final MedicineRepository medicineRepository;

    public MedicineQueryServiceImpl(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public List<Medicine> handle(GetAllMedicinesQuery query) {
        return medicineRepository.findAll();
    }

    @Override
    public Optional<Medicine> handle(GetMedicineByIdQuery query) {
        return medicineRepository.findById(query.id());
    }
}
