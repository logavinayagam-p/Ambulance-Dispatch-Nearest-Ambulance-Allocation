package com.example.ambulancedispatch.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "emergency_calls")
public class EmergencyCall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Caller name cannot be empty")
    @Column(nullable = false)
    private String callerName;

    @NotBlank(message = "Caller phone cannot be empty")
    @Column(nullable = false)
    private String callerPhone;

    @NotBlank(message = "Emergency type cannot be empty")
    @Column(nullable = false)
    private String emergencyType;

    @NotBlank(message = "Emergency location cannot be empty")
    @Column(nullable = false)
    private String emergencyLocation;

    @NotNull(message = "Zone must be selected")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(nullable = false)
    private LocalDateTime callTime = LocalDateTime.now();

    @Column(nullable = false)
    private String status = "PENDING";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_ambulance_id")
    private Ambulance assignedAmbulance;

    public EmergencyCall() {}

    public EmergencyCall(String callerName, String callerPhone, String emergencyType, String emergencyLocation, Zone zone) {
        this.callerName = callerName;
        this.callerPhone = callerPhone;
        this.emergencyType = emergencyType;
        this.emergencyLocation = emergencyLocation;
        this.zone = zone;
        this.callTime = LocalDateTime.now();
        this.status = "PENDING";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCallerName() { return callerName; }
    public void setCallerName(String callerName) { this.callerName = callerName; }
    public String getCallerPhone() { return callerPhone; }
    public void setCallerPhone(String callerPhone) { this.callerPhone = callerPhone; }
    public String getEmergencyType() { return emergencyType; }
    public void setEmergencyType(String emergencyType) { this.emergencyType = emergencyType; }
    public String getEmergencyLocation() { return emergencyLocation; }
    public void setEmergencyLocation(String emergencyLocation) { this.emergencyLocation = emergencyLocation; }
    public Zone getZone() { return zone; }
    public void setZone(Zone zone) { this.zone = zone; }
    public LocalDateTime getCallTime() { return callTime; }
    public void setCallTime(LocalDateTime callTime) { this.callTime = callTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Ambulance getAssignedAmbulance() { return assignedAmbulance; }
    public void setAssignedAmbulance(Ambulance assignedAmbulance) { this.assignedAmbulance = assignedAmbulance; }
}
