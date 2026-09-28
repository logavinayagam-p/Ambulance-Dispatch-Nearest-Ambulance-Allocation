package com.example.ambulancedispatch.controller;

import com.example.ambulancedispatch.entity.EmergencyCall;
import com.example.ambulancedispatch.service.DispatchService;
import com.example.ambulancedispatch.service.EmergencyCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/emergency-calls")
public class EmergencyCallRestController {

    private final EmergencyCallService emergencyCallService;
    private final DispatchService dispatchService;

    @Autowired
    public EmergencyCallRestController(EmergencyCallService emergencyCallService,
                                       DispatchService dispatchService) {
        this.emergencyCallService = emergencyCallService;
        this.dispatchService = dispatchService;
    }

    @GetMapping
    public List<EmergencyCall> getAll() { return emergencyCallService.getAllCalls(); }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyCall> getById(@PathVariable Long id) {
        return emergencyCallService.getCallById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public EmergencyCall create(@RequestBody EmergencyCall call) { return emergencyCallService.saveCall(call); }

    @PutMapping("/{id}")
    public ResponseEntity<EmergencyCall> update(@PathVariable Long id, @RequestBody EmergencyCall callDetails) {
        return emergencyCallService.getCallById(id).map(existing -> {
            existing.setCallerName(callDetails.getCallerName());
            existing.setCallerPhone(callDetails.getCallerPhone());
            existing.setEmergencyType(callDetails.getEmergencyType());
            existing.setEmergencyLocation(callDetails.getEmergencyLocation());
            if (callDetails.getZone() != null) existing.setZone(callDetails.getZone());
            return ResponseEntity.ok(emergencyCallService.saveCall(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (emergencyCallService.getCallById(id).isPresent()) {
            emergencyCallService.deleteCall(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/dispatch")
    public ResponseEntity<Map<String, String>> dispatch(@PathVariable Long id) {
        String result = dispatchService.findAndAssignNearestAmbulance(id);
        return ResponseEntity.ok(Map.of("message", result));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Map<String, String>> complete(@PathVariable Long id) {
        String result = dispatchService.completeEmergencyCall(id);
        return ResponseEntity.ok(Map.of("message", result));
    }
}
