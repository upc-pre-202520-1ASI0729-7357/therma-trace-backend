package com.thermatrace.thermatracebackend.medicines.domain.services;

import com.thermatrace.thermatracebackend.medicines.domain.model.aggregates.Medicine;
import com.thermatrace.thermatracebackend.medicines.domain.model.queries.GetAllMedicinesByUserIdQuery;
import com.thermatrace.thermatracebackend.medicines.domain.model.queries.GetAllMedicinesQuery;
import com.thermatrace.thermatracebackend.medicines.domain.model.queries.GetMedicineByIdAndUserIdQuery;
import com.thermatrace.thermatracebackend.medicines.domain.model.queries.GetMedicineByIdQuery;

import java.util.List;
import java.util.Optional;

public interface MedicineQueryService {
    List<Medicine> handle(GetAllMedicinesQuery query);
    Optional<Medicine> handle(GetMedicineByIdQuery query);
    List<Medicine> handle(GetAllMedicinesByUserIdQuery query);
    Optional<Medicine> handle(GetMedicineByIdAndUserIdQuery query);
}
