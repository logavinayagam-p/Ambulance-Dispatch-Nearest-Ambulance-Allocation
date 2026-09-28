package com.example.ambulancedispatch.controller;

import com.example.ambulancedispatch.service.AmbulanceService;
import com.example.ambulancedispatch.service.DispatchService;
import com.example.ambulancedispatch.service.EmergencyCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final AmbulanceService ambulanceService;
    private final EmergencyCallService emergencyCallService;
    private final DispatchService dispatchService;

    @Autowired
    public DashboardController(AmbulanceService ambulanceService,
                               EmergencyCallService emergencyCallService,
                               DispatchService dispatchService) {
        this.ambulanceService = ambulanceService;
        this.emergencyCallService = emergencyCallService;
        this.dispatchService = dispatchService;
    }

    @GetMapping({"/", "/dashboard"})
    public String showDashboard(Model model) {
        model.addAttribute("totalAmbulances", ambulanceService.countTotalAmbulances());
        model.addAttribute("availableAmbulances", ambulanceService.countAvailableAmbulances());
        model.addAttribute("busyAmbulances", ambulanceService.countBusyAmbulances());

        model.addAttribute("totalCalls", emergencyCallService.countTotalCalls());
        model.addAttribute("pendingCalls", emergencyCallService.countPendingCalls());
        model.addAttribute("dispatchedCalls", emergencyCallService.countDispatchedCalls());
        model.addAttribute("completedCalls", emergencyCallService.countCompletedCalls());

        model.addAttribute("recentCalls", emergencyCallService.getRecentCalls());
        model.addAttribute("recentDispatches", dispatchService.getRecentDispatches());
        return "dashboard";
    }
}
