package com.example.ambulancedispatch.repository;

import com.example.ambulancedispatch.entity.Ambulance;
import com.example.ambulancedispatch.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AmbulanceRepository extends JpaRepository<Ambulance, Long> {
    List<Ambulance> findByHomeZoneAndStatus(Zone homeZone, String status);
    List<Ambulance> findByHomeZoneInAndStatus(List<Zone> zones, String status);
    long countByStatus(String status);
    boolean existsByAmbulanceNumber(String ambulanceNumber);
    Optional<Ambulance> findByAmbulanceNumber(String ambulanceNumber);
}
