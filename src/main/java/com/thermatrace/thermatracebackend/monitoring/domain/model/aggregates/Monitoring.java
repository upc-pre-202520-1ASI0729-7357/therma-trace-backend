package com.thermatrace.thermatracebackend.monitoring.domain.model.aggregates;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medicine_monitoring")
@Getter
@Setter
public class Monitoring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long medicineId;

    @Column(nullable = false)
    private String medicineName;

    @Column(nullable = false)
    private Double temperatura;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private String ubicacion;

    protected Monitoring() {}

    public Monitoring(Long medicineId, String medicineName, Double temperatura, String estado, Integer stock, String ubicacion) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.temperatura = temperatura;
        this.estado = estado;
        this.stock = stock;
        this.ubicacion = ubicacion;
    }
}
