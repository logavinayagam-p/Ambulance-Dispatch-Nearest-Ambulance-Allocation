package com.example.ambulancedispatch.repository;

import com.example.ambulancedispatch.entity.EmergencyCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmergencyCallRepository extends JpaRepository<EmergencyCall, Long> {
    List<EmergencyCall> findByStatus(String status);
    long countByStatus(String status);
    List<EmergencyCall> findTop5ByOrderByCallTimeDesc();
    List<EmergencyCall> findAllByOrderByCallTimeDesc();
}
