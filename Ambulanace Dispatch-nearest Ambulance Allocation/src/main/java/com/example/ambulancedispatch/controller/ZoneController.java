package com.example.ambulancedispatch.controller;

import com.example.ambulancedispatch.entity.Zone;
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
@RequestMapping("/zones")
public class ZoneController {

    private final ZoneService zoneService;

    @Autowired
    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping
    public String listZones(Model model) {
        model.addAttribute("zones", zoneService.getAllZones());
        return "zones/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("zone", new Zone());
        model.addAttribute("pageTitle", "Add New Zone");
        return "zones/form";
    }

    @PostMapping("/save")
    public String saveZone(@Valid @ModelAttribute("zone") Zone zone,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", zone.getId() == null ? "Add New Zone" : "Edit Zone");
            return "zones/form";
        }
        zoneService.saveZone(zone);
        redirectAttributes.addFlashAttribute("successMessage", "Zone saved successfully!");
        return "redirect:/zones";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Zone> zone = zoneService.getZoneById(id);
        if (zone.isPresent()) {
            model.addAttribute("zone", zone.get());
            model.addAttribute("pageTitle", "Edit Zone");
            return "zones/form";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Zone not found with ID: " + id);
        return "redirect:/zones";
    }

    @GetMapping("/delete/{id}")
    public String deleteZone(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            zoneService.deleteZone(id);
            redirectAttributes.addFlashAttribute("successMessage", "Zone deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete zone because it is referenced by existing ambulances or calls.");
        }
        return "redirect:/zones";
    }
}
