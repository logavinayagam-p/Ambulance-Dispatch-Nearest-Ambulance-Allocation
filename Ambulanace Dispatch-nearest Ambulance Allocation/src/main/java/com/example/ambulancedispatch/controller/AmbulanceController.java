package com.example.ambulancedispatch.controller;

import com.example.ambulancedispatch.entity.Ambulance;
import com.example.ambulancedispatch.service.AmbulanceService;
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
@RequestMapping("/ambulances")
public class AmbulanceController {

    private final AmbulanceService ambulanceService;
    private final ZoneService zoneService;

    @Autowired
    public AmbulanceController(AmbulanceService ambulanceService, ZoneService zoneService) {
        this.ambulanceService = ambulanceService;
        this.zoneService = zoneService;
    }

    @GetMapping
    public String listAmbulances(Model model) {
        model.addAttribute("ambulances", ambulanceService.getAllAmbulances());
        return "ambulances/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("ambulance", new Ambulance());
        model.addAttribute("zones", zoneService.getAllZones());
        model.addAttribute("pageTitle", "Add New Ambulance");
        return "ambulances/form";
    }

    @PostMapping("/save")
    public String saveAmbulance(@Valid @ModelAttribute("ambulance") Ambulance ambulance,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("zones", zoneService.getAllZones());
            model.addAttribute("pageTitle", ambulance.getId() == null ? "Add New Ambulance" : "Edit Ambulance");
            return "ambulances/form";
        }
        ambulanceService.saveAmbulance(ambulance);
        redirectAttributes.addFlashAttribute("successMessage", "Ambulance saved successfully!");
        return "redirect:/ambulances";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Ambulance> ambulance = ambulanceService.getAmbulanceById(id);
        if (ambulance.isPresent()) {
            model.addAttribute("ambulance", ambulance.get());
            model.addAttribute("zones", zoneService.getAllZones());
            model.addAttribute("pageTitle", "Edit Ambulance");
            return "ambulances/form";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Ambulance not found with ID: " + id);
        return "redirect:/ambulances";
    }

    @GetMapping("/view/{id}")
    public String viewAmbulance(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Ambulance> ambulance = ambulanceService.getAmbulanceById(id);
        if (ambulance.isPresent()) {
            model.addAttribute("ambulance", ambulance.get());
            return "ambulances/view";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Ambulance not found with ID: " + id);
        return "redirect:/ambulances";
    }

    @GetMapping("/delete/{id}")
    public String deleteAmbulance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ambulanceService.deleteAmbulance(id);
            redirectAttributes.addFlashAttribute("successMessage", "Ambulance deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete ambulance because it has linked emergency assignments.");
        }
        return "redirect:/ambulances";
    }

    @GetMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Ambulance> opt = ambulanceService.getAmbulanceById(id);
        if (opt.isPresent()) {
            Ambulance amb = opt.get();
            String newStatus = "AVAILABLE".equalsIgnoreCase(amb.getStatus()) ? "BUSY" : "AVAILABLE";
            amb.setStatus(newStatus);
            ambulanceService.saveAmbulance(amb);
            redirectAttributes.addFlashAttribute("successMessage", "Ambulance status updated to " + newStatus);
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Ambulance not found.");
        }
        return "redirect:/ambulances";
    }
}
