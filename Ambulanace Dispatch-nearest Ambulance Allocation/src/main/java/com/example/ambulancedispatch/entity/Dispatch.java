package com.example.ambulancedispatch.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dispatches")
public class Dispatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emergency_call_id", nullable = false)
    private EmergencyCall emergencyCall;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ambulance_id", nullable = false)
    private Ambulance ambulance;

    @Column(nullable = false)
    private LocalDateTime dispatchTime = LocalDateTime.now();

    private String responseTime;

    @Column(nullable = false)
    private String status = "DISPATCHED";

    public Dispatch() {}

    public Dispatch(EmergencyCall emergencyCall, Ambulance ambulance, LocalDateTime dispatchTime, String responseTime, String status) {
        this.emergencyCall = emergencyCall;
        this.ambulance = ambulance;
        this.dispatchTime = dispatchTime;
        this.responseTime = responseTime;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public EmergencyCall getEmergencyCall() { return emergencyCall; }
    public void setEmergencyCall(EmergencyCall emergencyCall) { this.emergencyCall = emergencyCall; }
    public Ambulance getAmbulance() { return ambulance; }
    public void setAmbulance(Ambulance ambulance) { this.ambulance = ambulance; }
    public LocalDateTime getDispatchTime() { return dispatchTime; }
    public void setDispatchTime(LocalDateTime dispatchTime) { this.dispatchTime = dispatchTime; }
    public String getResponseTime() { return responseTime; }
    public void setResponseTime(String responseTime) { this.responseTime = responseTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
