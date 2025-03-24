package com.lumiring.minimacs.dao.device;

import com.lumiring.minimacs.entity.device.ControlPointEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ControlPointRepository extends JpaRepository<ControlPointEntity, Long> {

    boolean existsByControlPointIdxAndDeviceEntityId(Long controlPointIdx, Long deviceId);

    Optional<ControlPointEntity> findByControlPointIdx(Long controlPointIdx);

    @EntityGraph(attributePaths = {"deviceEntity"})
    Page<ControlPointEntity> findByShelterTrue(Pageable pageable);

    boolean existsByControlPointIdxAndDeviceEntityDeviceIdx(Long controlPointIdx, Long deviceIdx);


}
