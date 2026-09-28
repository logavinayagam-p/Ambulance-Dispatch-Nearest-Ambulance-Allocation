package com.example.ambulancedispatch.service;

import com.example.ambulancedispatch.entity.Ambulance;
import com.example.ambulancedispatch.entity.Zone;
import com.example.ambulancedispatch.repository.AmbulanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AmbulanceService {

    private final AmbulanceRepository ambulanceRepository;

    @Autowired
    public AmbulanceService(AmbulanceRepository ambulanceRepository) {
        this.ambulanceRepository = ambulanceRepository;
    }

    public List<Ambulance> getAllAmbulances() { return ambulanceRepository.findAll(); }
    public Optional<Ambulance> getAmbulanceById(Long id) { return ambulanceRepository.findById(id); }
    public Ambulance saveAmbulance(Ambulance ambulance) { return ambulanceRepository.save(ambulance); }
    public void deleteAmbulance(Long id) { ambulanceRepository.deleteById(id); }

    public boolean updateStatus(Long id, String status) {
        Optional<Ambulance> optional = ambulanceRepository.findById(id);
        if (optional.isPresent()) {
            Ambulance ambulance = optional.get();
            ambulance.setStatus(status);
            ambulanceRepository.save(ambulance);
            return true;
        }
        return false;
    }

    public List<Ambulance> getAvailableAmbulancesInZone(Zone zone) {
        return ambulanceRepository.findByHomeZoneAndStatus(zone, "AVAILABLE");
    }

    public long countTotalAmbulances() { return ambulanceRepository.count(); }
    public long countAvailableAmbulances() { return ambulanceRepository.countByStatus("AVAILABLE"); }
    public long countBusyAmbulances() { return ambulanceRepository.countByStatus("BUSY"); }
}
