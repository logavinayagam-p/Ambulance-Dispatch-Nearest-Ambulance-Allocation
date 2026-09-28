package com.example.ambulancedispatch.controller;

import com.example.ambulancedispatch.entity.Zone;
import com.example.ambulancedispatch.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class ZoneRestController {

    private final ZoneService zoneService;

    @Autowired
    public ZoneRestController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping
    public List<Zone> getAll() { return zoneService.getAllZones(); }

    @GetMapping("/{id}")
    public ResponseEntity<Zone> getById(@PathVariable Long id) {
        return zoneService.getZoneById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Zone create(@RequestBody Zone zone) { return zoneService.saveZone(zone); }

    @PutMapping("/{id}")
    public ResponseEntity<Zone> update(@PathVariable Long id, @RequestBody Zone zoneDetails) {
        return zoneService.getZoneById(id).map(existing -> {
            existing.setZoneName(zoneDetails.getZoneName());
            existing.setZoneCode(zoneDetails.getZoneCode());
            existing.setDescription(zoneDetails.getDescription());
            return ResponseEntity.ok(zoneService.saveZone(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (zoneService.getZoneById(id).isPresent()) {
            zoneService.deleteZone(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
