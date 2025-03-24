package com.lumiring.minimacs.entity.device;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "control_point", schema = "device")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControlPointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "control_point_idx", nullable = false, unique = true)
    private Long controlPointIdx; // Уникальный идентификатор контрольной точки

    @Column(name = "state", nullable = false)
    private DeviceState state; // Статус точки

    @Column(name = "alarmed", nullable = false)
    private boolean alarmed; // под деймствием тревоги

    @Column(name = "shelter", nullable = false)
    private boolean shelter; // убежище или нет


    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @ManyToOne
    @JoinColumn(name = "device_entity_id", nullable = false)
    private DeviceEntity deviceEntity; // Связь с устройством

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

}

