package com.example.ambulancedispatch.controller;

import com.example.ambulancedispatch.entity.Dispatch;
import com.example.ambulancedispatch.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequestMapping("/dispatches")
public class DispatchController {

    private final DispatchService dispatchService;

    @Autowired
    public DispatchController(DispatchService dispatchService) {
        this.dispatchService = dispatchService;
    }

    @GetMapping
    public String listDispatches(Model model) {
        model.addAttribute("dispatches", dispatchService.getAllDispatches());
        return "dispatches/list";
    }

    @GetMapping("/view/{id}")
    public String viewDispatch(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Dispatch> dispatch = dispatchService.getDispatchById(id);
        if (dispatch.isPresent()) {
            model.addAttribute("dispatch", dispatch.get());
            return "dispatches/view";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Dispatch record not found with ID: " + id);
        return "redirect:/dispatches";
    }

    @GetMapping("/delete/{id}")
    public String deleteDispatch(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            dispatchService.deleteDispatch(id);
            redirectAttributes.addFlashAttribute("successMessage", "Dispatch record deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting dispatch record.");
        }
        return "redirect:/dispatches";
    }
}
