package com.example.ambulancedispatch.service;

import com.example.ambulancedispatch.entity.EmergencyCall;
import com.example.ambulancedispatch.repository.EmergencyCallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EmergencyCallService {

    private final EmergencyCallRepository emergencyCallRepository;

    @Autowired
    public EmergencyCallService(EmergencyCallRepository emergencyCallRepository) {
        this.emergencyCallRepository = emergencyCallRepository;
    }

    public List<EmergencyCall> getAllCalls() { return emergencyCallRepository.findAllByOrderByCallTimeDesc(); }
    public Optional<EmergencyCall> getCallById(Long id) { return emergencyCallRepository.findById(id); }
    public EmergencyCall saveCall(EmergencyCall call) { return emergencyCallRepository.save(call); }
    public void deleteCall(Long id) { emergencyCallRepository.deleteById(id); }
    public List<EmergencyCall> getRecentCalls() { return emergencyCallRepository.findTop5ByOrderByCallTimeDesc(); }
    public long countTotalCalls() { return emergencyCallRepository.count(); }
    public long countPendingCalls() { return emergencyCallRepository.countByStatus("PENDING"); }
    public long countDispatchedCalls() { return emergencyCallRepository.countByStatus("DISPATCHED"); }
    public long countCompletedCalls() { return emergencyCallRepository.countByStatus("COMPLETED"); }
}
