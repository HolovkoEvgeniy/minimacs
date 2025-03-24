package com.lumiring.minimacs.entity.device;



import com.lumiring.minimacs.entity.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "device_entity", schema = "device")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "deviceEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ControlPointEntity> controlPoints = new ArrayList<>();

    @Column(name = "device_idx", nullable = false, unique = true)
    private Long deviceIdx;

    @Column(name = "location")
    private String location;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

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

