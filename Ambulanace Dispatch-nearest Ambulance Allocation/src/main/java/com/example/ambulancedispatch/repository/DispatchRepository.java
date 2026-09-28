package com.example.ambulancedispatch.repository;

import com.example.ambulancedispatch.entity.Dispatch;
import com.example.ambulancedispatch.entity.EmergencyCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DispatchRepository extends JpaRepository<Dispatch, Long> {
    List<Dispatch> findByEmergencyCall(EmergencyCall emergencyCall);
    Optional<Dispatch> findTopByEmergencyCallOrderByIdDesc(EmergencyCall emergencyCall);
    List<Dispatch> findTop5ByOrderByDispatchTimeDesc();
    List<Dispatch> findAllByOrderByDispatchTimeDesc();
}
