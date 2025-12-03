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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "medicine_id", nullable = false)
    private Long medicineId;

    @Column(name = "temperature", nullable = false)
    private Double temperature;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "location", nullable = false)
    private String location;

    protected Monitoring() {}

    public Monitoring(Long userId, Long medicineId, Double temperature, String state, Integer stock, String location) {
        this.userId = userId;
        this.medicineId = medicineId;
        this.temperature = temperature;
        this.state = state;
        this.stock = stock;
        this.location = location;
    }
}
