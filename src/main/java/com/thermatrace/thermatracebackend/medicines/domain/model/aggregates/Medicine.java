package com.thermatrace.thermatracebackend.medicines.domain.model.aggregates;

import com.thermatrace.thermatracebackend.medicines.domain.model.valueobjects.ImageUrl;
import com.thermatrace.thermatracebackend.medicines.domain.model.valueobjects.MedicineName;
import com.thermatrace.thermatracebackend.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "medicines")
@Getter
@Setter
public class Medicine extends AuditableAbstractAggregateRoot<Medicine> {

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "name", nullable = false))
    })
    private MedicineName medicineName;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "url", column = @Column(name = "image_url"))
    })
    private ImageUrl imageUrl;

    protected Medicine() {
        // Required by JPA
    }

    public Medicine(Long userId, MedicineName medicineName, LocalDate expirationDate, ImageUrl imageUrl) {
        this.userId = userId;
        this.medicineName = medicineName;
        this.expirationDate = expirationDate;
        this.imageUrl = imageUrl;
    }

    public Medicine(Long userId, String name, LocalDate expirationDate, String imageUrl) {
        this(userId,
             new MedicineName(name),
             expirationDate,
             new ImageUrl(imageUrl));
    }

    public String getName() {
        return medicineName != null ? medicineName.getName() : "";
    }

    public String getImageUrlValue() {
        return imageUrl != null ? imageUrl.getUrl() : "";
    }

    public void updateMedicine(String name, LocalDate expirationDate, String imageUrl) {
        this.medicineName = new MedicineName(name);
        this.expirationDate = expirationDate;
        this.imageUrl = new ImageUrl(imageUrl);
    }
}
