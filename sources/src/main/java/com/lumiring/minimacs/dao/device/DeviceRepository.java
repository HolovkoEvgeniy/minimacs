package com.lumiring.minimacs.dao.device;

import com.lumiring.minimacs.domain.dto.device.StateReport;
import com.lumiring.minimacs.entity.device.DeviceEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {

    // Поиск устройства по user_id
    Page<DeviceEntity> findByUserId(Long userId, Pageable pageable);


    // Поиск по control_point_idx (уникальное поле)
    Optional<DeviceEntity> findByDeviceIdx(Long deviceIdx);


    @Modifying
    @Transactional
    @Query("UPDATE DeviceEntity d SET d.lastHeartbeat = :now WHERE d.deviceIdx = :deviceIdx")
    void updateLastHeartbeat(@Param("deviceIdx") Long deviceIdx, @Param("now") LocalDateTime now);



    @Modifying
    @Transactional
    @Query("UPDATE ControlPointEntity c SET c.alarmed = :alarmed WHERE c.controlPointIdx = :controlPointIdx")
    void updateAlarmed(@Param("controlPointIdx") String controlPointIdx, @Param("alarmed") boolean alarmed);

    @Query("""
                SELECT new com.lumiring.minimacs.domain.dto.device.StateReport(
                    c.controlPointIdx,
                    c.alarmed,
                    c.state,
                    d.lastHeartbeat
                )
                FROM ControlPointEntity c
                JOIN c.deviceEntity d
                WHERE d.user.id = :userId
            """)
    List<StateReport> getStateReportList(@Param("userId") Long userId);
}

