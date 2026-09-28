package com.example.ambulancedispatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ambulances")
public class Ambulance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ambulance number cannot be empty")
    @Column(nullable = false, unique = true)
    private String ambulanceNumber;

    @NotBlank(message = "Driver name cannot be empty")
    @Column(nullable = false)
    private String driverName;

    @NotBlank(message = "Driver phone cannot be empty")
    @Column(nullable = false)
    private String driverPhone;

    @NotBlank(message = "Ambulance type is required")
    @Column(nullable = false)
    private String ambulanceType;

    @NotNull(message = "Home zone must be selected")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone homeZone;

    @Column(nullable = false)
    private String status = "AVAILABLE";

    public Ambulance() {}

    public Ambulance(String ambulanceNumber, String driverName, String driverPhone, String ambulanceType, Zone homeZone, String status) {
        this.ambulanceNumber = ambulanceNumber;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
        this.ambulanceType = ambulanceType;
        this.homeZone = homeZone;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAmbulanceNumber() { return ambulanceNumber; }
    public void setAmbulanceNumber(String ambulanceNumber) { this.ambulanceNumber = ambulanceNumber; }
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public String getDriverPhone() { return driverPhone; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }
    public String getAmbulanceType() { return ambulanceType; }
    public void setAmbulanceType(String ambulanceType) { this.ambulanceType = ambulanceType; }
    public Zone getHomeZone() { return homeZone; }
    public void setHomeZone(Zone homeZone) { this.homeZone = homeZone; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
