package com.example.ambulancedispatch.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zones")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Zone name is required")
    @Column(nullable = false)
    private String zoneName;

    @NotBlank(message = "Zone code is required")
    @Column(nullable = false, unique = true)
    private String zoneCode;

    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "homeZone", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Ambulance> ambulances = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<EmergencyCall> emergencyCalls = new ArrayList<>();

    public Zone() {}

    public Zone(String zoneName, String zoneCode, String description) {
        this.zoneName = zoneName;
        this.zoneCode = zoneCode;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
    public String getZoneCode() { return zoneCode; }
    public void setZoneCode(String zoneCode) { this.zoneCode = zoneCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Ambulance> getAmbulances() { return ambulances; }
    public void setAmbulances(List<Ambulance> ambulances) { this.ambulances = ambulances; }
    public List<EmergencyCall> getEmergencyCalls() { return emergencyCalls; }
    public void setEmergencyCalls(List<EmergencyCall> emergencyCalls) { this.emergencyCalls = emergencyCalls; }
}
