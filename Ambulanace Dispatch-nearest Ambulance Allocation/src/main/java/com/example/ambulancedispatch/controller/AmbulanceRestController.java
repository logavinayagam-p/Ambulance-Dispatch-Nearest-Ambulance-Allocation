package com.example.ambulancedispatch.controller;

import com.example.ambulancedispatch.entity.Ambulance;
import com.example.ambulancedispatch.service.AmbulanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ambulances")
public class AmbulanceRestController {

    private final AmbulanceService ambulanceService;

    @Autowired
    public AmbulanceRestController(AmbulanceService ambulanceService) {
        this.ambulanceService = ambulanceService;
    }

    @GetMapping
    public List<Ambulance> getAll() { return ambulanceService.getAllAmbulances(); }

    @GetMapping("/{id}")
    public ResponseEntity<Ambulance> getById(@PathVariable Long id) {
        return ambulanceService.getAmbulanceById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Ambulance create(@RequestBody Ambulance ambulance) {
        return ambulanceService.saveAmbulance(ambulance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Ambulance> update(@PathVariable Long id, @RequestBody Ambulance ambulanceDetails) {
        return ambulanceService.getAmbulanceById(id).map(existing -> {
            existing.setAmbulanceNumber(ambulanceDetails.getAmbulanceNumber());
            existing.setDriverName(ambulanceDetails.getDriverName());
            existing.setDriverPhone(ambulanceDetails.getDriverPhone());
            existing.setAmbulanceType(ambulanceDetails.getAmbulanceType());
            existing.setStatus(ambulanceDetails.getStatus());
            if (ambulanceDetails.getHomeZone() != null) existing.setHomeZone(ambulanceDetails.getHomeZone());
            return ResponseEntity.ok(ambulanceService.saveAmbulance(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (ambulanceService.getAmbulanceById(id).isPresent()) {
            ambulanceService.deleteAmbulance(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
