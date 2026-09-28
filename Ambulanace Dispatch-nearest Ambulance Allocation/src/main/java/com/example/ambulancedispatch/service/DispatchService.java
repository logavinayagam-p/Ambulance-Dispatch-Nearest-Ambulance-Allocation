package com.example.ambulancedispatch.service;

import com.example.ambulancedispatch.entity.Ambulance;
import com.example.ambulancedispatch.entity.Dispatch;
import com.example.ambulancedispatch.entity.EmergencyCall;
import com.example.ambulancedispatch.entity.Zone;
import com.example.ambulancedispatch.repository.AmbulanceRepository;
import com.example.ambulancedispatch.repository.DispatchRepository;
import com.example.ambulancedispatch.repository.EmergencyCallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DispatchService {

    private final DispatchRepository dispatchRepository;
    private final EmergencyCallRepository emergencyCallRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final ZoneService zoneService;

    @Autowired
    public DispatchService(DispatchRepository dispatchRepository,
                           EmergencyCallRepository emergencyCallRepository,
                           AmbulanceRepository ambulanceRepository,
                           ZoneService zoneService) {
        this.dispatchRepository = dispatchRepository;
        this.emergencyCallRepository = emergencyCallRepository;
        this.ambulanceRepository = ambulanceRepository;
        this.zoneService = zoneService;
    }

    public List<Dispatch> getAllDispatches() { return dispatchRepository.findAllByOrderByDispatchTimeDesc(); }
    public List<Dispatch> getRecentDispatches() { return dispatchRepository.findTop5ByOrderByDispatchTimeDesc(); }
    public Optional<Dispatch> getDispatchById(Long id) { return dispatchRepository.findById(id); }
    public void deleteDispatch(Long id) { dispatchRepository.deleteById(id); }

    @Transactional
    public String findAndAssignNearestAmbulance(Long emergencyCallId) {
        Optional<EmergencyCall> callOptional = emergencyCallRepository.findById(emergencyCallId);
        if (callOptional.isEmpty()) return "Emergency call not found.";

        EmergencyCall call = callOptional.get();
        if ("DISPATCHED".equalsIgnoreCase(call.getStatus())) {
            return "Emergency call is already dispatched to ambulance " +
                    (call.getAssignedAmbulance() != null ? call.getAssignedAmbulance().getAmbulanceNumber() : "") + ".";
        }
        if ("COMPLETED".equalsIgnoreCase(call.getStatus())) {
            return "This emergency call has already been completed.";
        }

        Zone emergencyZone = call.getZone();
        Ambulance selectedAmbulance = null;
        String allocationReason = "";

        List<Ambulance> sameZoneAmbulances = ambulanceRepository.findByHomeZoneAndStatus(emergencyZone, "AVAILABLE");
        if (!sameZoneAmbulances.isEmpty()) {
            selectedAmbulance = sameZoneAmbulances.get(0);
            allocationReason = "Assigned from same zone (" + emergencyZone.getZoneName() + ")";
        } else {
            List<Zone> nearbyZones = zoneService.getNearbyZones(emergencyZone);
            for (Zone nearby : nearbyZones) {
                List<Ambulance> nearbyAmbulances = ambulanceRepository.findByHomeZoneAndStatus(nearby, "AVAILABLE");
                if (!nearbyAmbulances.isEmpty()) {
                    selectedAmbulance = nearbyAmbulances.get(0);
                    allocationReason = "Assigned from nearby zone (" + nearby.getZoneName() + ")";
                    break;
                }
            }
        }

        if (selectedAmbulance == null) {
            return "No available ambulance found in " + emergencyZone.getZoneName() + " or nearby zones.";
        }

        selectedAmbulance.setStatus("BUSY");
        ambulanceRepository.save(selectedAmbulance);

        call.setStatus("DISPATCHED");
        call.setAssignedAmbulance(selectedAmbulance);
        emergencyCallRepository.save(call);

        Dispatch dispatch = new Dispatch();
        dispatch.setEmergencyCall(call);
        dispatch.setAmbulance(selectedAmbulance);
        dispatch.setDispatchTime(LocalDateTime.now());
        dispatch.setResponseTime("Immediate");
        dispatch.setStatus("DISPATCHED");
        dispatchRepository.save(dispatch);

        return "SUCCESS: Ambulance " + selectedAmbulance.getAmbulanceNumber() +
                " (" + selectedAmbulance.getDriverName() + ") successfully dispatched! " + allocationReason;
    }

    @Transactional
    public String completeEmergencyCall(Long emergencyCallId) {
        Optional<EmergencyCall> callOptional = emergencyCallRepository.findById(emergencyCallId);
        if (callOptional.isEmpty()) return "Emergency call not found.";

        EmergencyCall call = callOptional.get();
        if ("COMPLETED".equalsIgnoreCase(call.getStatus())) return "Emergency call is already completed.";

        Ambulance ambulance = call.getAssignedAmbulance();
        if (ambulance != null) {
            ambulance.setStatus("AVAILABLE");
            ambulanceRepository.save(ambulance);
        }

        call.setStatus("COMPLETED");
        emergencyCallRepository.save(call);

        Optional<Dispatch> dispatchOptional = dispatchRepository.findTopByEmergencyCallOrderByIdDesc(call);
        dispatchOptional.ifPresent(dispatch -> {
            dispatch.setStatus("COMPLETED");
            dispatchRepository.save(dispatch);
        });

        return "SUCCESS: Emergency call #" + call.getId() + " marked as COMPLETED. Ambulance is now AVAILABLE.";
    }

    public boolean updateDispatchStatus(Long id, String status) {
        Optional<Dispatch> optional = dispatchRepository.findById(id);
        if (optional.isPresent()) {
            Dispatch dispatch = optional.get();
            dispatch.setStatus(status);
            dispatchRepository.save(dispatch);
            return true;
        }
        return false;
    }
}
