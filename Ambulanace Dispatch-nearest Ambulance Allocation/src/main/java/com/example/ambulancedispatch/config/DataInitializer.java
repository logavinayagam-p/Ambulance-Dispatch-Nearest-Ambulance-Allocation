package com.example.ambulancedispatch.config;

import com.example.ambulancedispatch.entity.Ambulance;
import com.example.ambulancedispatch.entity.Dispatch;
import com.example.ambulancedispatch.entity.EmergencyCall;
import com.example.ambulancedispatch.entity.Zone;
import com.example.ambulancedispatch.repository.AmbulanceRepository;
import com.example.ambulancedispatch.repository.DispatchRepository;
import com.example.ambulancedispatch.repository.EmergencyCallRepository;
import com.example.ambulancedispatch.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ZoneRepository zoneRepository;
    private final AmbulanceRepository ambulanceRepository;
    private final EmergencyCallRepository emergencyCallRepository;
    private final DispatchRepository dispatchRepository;

    @Autowired
    public DataInitializer(ZoneRepository zoneRepository,
                           AmbulanceRepository ambulanceRepository,
                           EmergencyCallRepository emergencyCallRepository,
                           DispatchRepository dispatchRepository) {
        this.zoneRepository = zoneRepository;
        this.ambulanceRepository = ambulanceRepository;
        this.emergencyCallRepository = emergencyCallRepository;
        this.dispatchRepository = dispatchRepository;
    }

    @Override
    public void run(String... args) {
        if (zoneRepository.count() > 0) return;

        System.out.println(">>> Seeding initial sample data for college demonstration...");

        Zone zoneA = new Zone("Zone A", "ZONE_A", "North District / Downtown");
        Zone zoneB = new Zone("Zone B", "ZONE_B", "East Sector / Commercial Area");
        Zone zoneC = new Zone("Zone C", "ZONE_C", "Central City / Financial Hub");
        Zone zoneD = new Zone("Zone D", "ZONE_D", "West Suburbs / Residential Area");
        Zone zoneE = new Zone("Zone E", "ZONE_E", "South Quarter / Industrial Estate");

        zoneA = zoneRepository.save(zoneA);
        zoneB = zoneRepository.save(zoneB);
        zoneC = zoneRepository.save(zoneC);
        zoneD = zoneRepository.save(zoneD);
        zoneE = zoneRepository.save(zoneE);

        Ambulance amb1 = new Ambulance("AMB001", "Arun", "9876543210", "Advanced Life Support", zoneA, "AVAILABLE");
        Ambulance amb2 = new Ambulance("AMB002", "Kumar", "9876543211", "Basic Life Support", zoneB, "AVAILABLE");
        Ambulance amb3 = new Ambulance("AMB003", "Ravi", "9876543212", "Advanced Life Support", zoneC, "BUSY");
        Ambulance amb4 = new Ambulance("AMB004", "Suresh", "9876543213", "Basic Life Support", zoneD, "AVAILABLE");
        Ambulance amb5 = new Ambulance("AMB005", "Vijay", "9876543214", "Patient Transport", zoneE, "AVAILABLE");

        amb1 = ambulanceRepository.save(amb1);
        amb2 = ambulanceRepository.save(amb2);
        amb3 = ambulanceRepository.save(amb3);
        amb4 = ambulanceRepository.save(amb4);
        amb5 = ambulanceRepository.save(amb5);

        EmergencyCall call1 = new EmergencyCall("Ramesh", "9123456780", "Cardiac", "Block 4, North Park Avenue", zoneA);
        call1.setStatus("PENDING");
        emergencyCallRepository.save(call1);

        EmergencyCall call2 = new EmergencyCall("Priya", "9123456781", "Accident", "Market Road Junction", zoneB);
        call2.setStatus("PENDING");
        emergencyCallRepository.save(call2);

        EmergencyCall call3 = new EmergencyCall("Karthik", "9123456782", "Trauma", "Main Boulevard Tower 2", zoneC);
        call3.setStatus("DISPATCHED");
        call3.setAssignedAmbulance(amb3);
        call3 = emergencyCallRepository.save(call3);

        Dispatch disp3 = new Dispatch(call3, amb3, LocalDateTime.now().minusMinutes(25), "Immediate", "DISPATCHED");
        dispatchRepository.save(disp3);

        EmergencyCall call4 = new EmergencyCall("Sneha", "9123456783", "Respiratory", "Green Valley Villa 12", zoneD);
        call4.setStatus("PENDING");
        emergencyCallRepository.save(call4);

        EmergencyCall call5 = new EmergencyCall("Mohan", "9123456784", "General", "5th Cross Industrial Gate", zoneE);
        call5.setStatus("COMPLETED");
        call5.setAssignedAmbulance(amb5);
        call5 = emergencyCallRepository.save(call5);

        Dispatch disp5 = new Dispatch(call5, amb5, LocalDateTime.now().minusHours(2), "5 mins", "COMPLETED");
        dispatchRepository.save(disp5);

        System.out.println(">>> Sample data successfully loaded: 5 Zones, 5 Ambulances, 5 Emergency Calls.");
    }
}
