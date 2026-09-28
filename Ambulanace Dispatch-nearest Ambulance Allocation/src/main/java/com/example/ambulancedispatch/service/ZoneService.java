package com.example.ambulancedispatch.service;

import com.example.ambulancedispatch.entity.Zone;
import com.example.ambulancedispatch.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private static final Map<String, List<String>> NEARBY_ZONE_MAP = new HashMap<>();

    static {
        NEARBY_ZONE_MAP.put("ZONE_A", Arrays.asList("ZONE_B", "ZONE_C"));
        NEARBY_ZONE_MAP.put("Zone A", Arrays.asList("Zone B", "Zone C"));
        NEARBY_ZONE_MAP.put("A", Arrays.asList("B", "C"));

        NEARBY_ZONE_MAP.put("ZONE_B", Arrays.asList("ZONE_A", "ZONE_C", "ZONE_D"));
        NEARBY_ZONE_MAP.put("Zone B", Arrays.asList("Zone A", "Zone C", "Zone D"));
        NEARBY_ZONE_MAP.put("B", Arrays.asList("A", "C", "D"));

        NEARBY_ZONE_MAP.put("ZONE_C", Arrays.asList("ZONE_A", "ZONE_B", "ZONE_D"));
        NEARBY_ZONE_MAP.put("Zone C", Arrays.asList("Zone A", "Zone B", "Zone D"));
        NEARBY_ZONE_MAP.put("C", Arrays.asList("A", "B", "D"));

        NEARBY_ZONE_MAP.put("ZONE_D", Arrays.asList("ZONE_B", "ZONE_C", "ZONE_E"));
        NEARBY_ZONE_MAP.put("Zone D", Arrays.asList("Zone B", "Zone C", "Zone E"));
        NEARBY_ZONE_MAP.put("D", Arrays.asList("B", "C", "E"));

        NEARBY_ZONE_MAP.put("ZONE_E", Arrays.asList("ZONE_C", "ZONE_D"));
        NEARBY_ZONE_MAP.put("Zone E", Arrays.asList("Zone C", "Zone D"));
        NEARBY_ZONE_MAP.put("E", Arrays.asList("C", "D"));
    }

    @Autowired
    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<Zone> getAllZones() { return zoneRepository.findAll(); }
    public Optional<Zone> getZoneById(Long id) { return zoneRepository.findById(id); }
    public Zone saveZone(Zone zone) { return zoneRepository.save(zone); }
    public void deleteZone(Long id) { zoneRepository.deleteById(id); }

    public List<Zone> getNearbyZones(Zone sourceZone) {
        List<Zone> nearbyZones = new ArrayList<>();
        if (sourceZone == null) return nearbyZones;

        String code = sourceZone.getZoneCode() != null ? sourceZone.getZoneCode().trim() : "";
        String name = sourceZone.getZoneName() != null ? sourceZone.getZoneName().trim() : "";

        List<String> targetKeys = NEARBY_ZONE_MAP.get(code);
        if (targetKeys == null || targetKeys.isEmpty()) {
            targetKeys = NEARBY_ZONE_MAP.get(name);
        }

        if (targetKeys != null && !targetKeys.isEmpty()) {
            for (String targetKey : targetKeys) {
                for (Zone z : zoneRepository.findAll()) {
                    if (z.getId().equals(sourceZone.getId())) continue;
                    if (targetKey.equalsIgnoreCase(z.getZoneCode()) || targetKey.equalsIgnoreCase(z.getZoneName())) {
                        if (!nearbyZones.contains(z)) nearbyZones.add(z);
                    }
                }
            }
        }

        if (nearbyZones.isEmpty()) {
            for (Zone z : zoneRepository.findAll()) {
                if (!z.getId().equals(sourceZone.getId())) nearbyZones.add(z);
            }
        }
        return nearbyZones;
    }
}
