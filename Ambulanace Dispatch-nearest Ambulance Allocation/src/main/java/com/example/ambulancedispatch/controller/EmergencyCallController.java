package com.example.ambulancedispatch.controller;

import com.example.ambulancedispatch.entity.EmergencyCall;
import com.example.ambulancedispatch.service.DispatchService;
import com.example.ambulancedispatch.service.EmergencyCallService;
import com.example.ambulancedispatch.service.ZoneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/emergency-calls")
public class EmergencyCallController {

    private final EmergencyCallService emergencyCallService;
    private final ZoneService zoneService;
    private final DispatchService dispatchService;

    @Autowired
    public EmergencyCallController(EmergencyCallService emergencyCallService,
                                   ZoneService zoneService,
                                   DispatchService dispatchService) {
        this.emergencyCallService = emergencyCallService;
        this.zoneService = zoneService;
        this.dispatchService = dispatchService;
    }

    @GetMapping
    public String listEmergencyCalls(Model model) {
        model.addAttribute("calls", emergencyCallService.getAllCalls());
        return "emergency-calls/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("emergencyCall", new EmergencyCall());
        model.addAttribute("zones", zoneService.getAllZones());
        model.addAttribute("pageTitle", "Log Emergency Call");
        return "emergency-calls/form";
    }

    @PostMapping("/save")
    public String saveEmergencyCall(@Valid @ModelAttribute("emergencyCall") EmergencyCall emergencyCall,
                                    BindingResult result,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("zones", zoneService.getAllZones());
            model.addAttribute("pageTitle", emergencyCall.getId() == null ? "Log Emergency Call" : "Edit Emergency Call");
            return "emergency-calls/form";
        }
        emergencyCallService.saveCall(emergencyCall);
        redirectAttributes.addFlashAttribute("successMessage", "Emergency call saved successfully!");
        return "redirect:/emergency-calls";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<EmergencyCall> call = emergencyCallService.getCallById(id);
        if (call.isPresent()) {
            model.addAttribute("emergencyCall", call.get());
            model.addAttribute("zones", zoneService.getAllZones());
            model.addAttribute("pageTitle", "Edit Emergency Call");
            return "emergency-calls/form";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Call not found with ID: " + id);
        return "redirect:/emergency-calls";
    }

    @GetMapping("/view/{id}")
    public String viewEmergencyCall(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<EmergencyCall> call = emergencyCallService.getCallById(id);
        if (call.isPresent()) {
            model.addAttribute("call", call.get());
            return "emergency-calls/view";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Call not found with ID: " + id);
        return "redirect:/emergency-calls";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmergencyCall(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            emergencyCallService.deleteCall(id);
            redirectAttributes.addFlashAttribute("successMessage", "Emergency call deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete emergency call because it is linked to dispatch records.");
        }
        return "redirect:/emergency-calls";
    }

    @PostMapping("/dispatch/{id}")
    public String dispatchAmbulance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String result = dispatchService.findAndAssignNearestAmbulance(id);
        if (result.startsWith("SUCCESS")) {
            redirectAttributes.addFlashAttribute("successMessage", result.replace("SUCCESS: ", ""));
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", result);
        }
        return "redirect:/emergency-calls";
    }

    @PostMapping("/complete/{id}")
    public String completeCall(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String result = dispatchService.completeEmergencyCall(id);
        if (result.startsWith("SUCCESS")) {
            redirectAttributes.addFlashAttribute("successMessage", result.replace("SUCCESS: ", ""));
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", result);
        }
        return "redirect:/emergency-calls";
    }
}
