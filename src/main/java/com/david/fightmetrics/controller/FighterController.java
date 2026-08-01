package com.david.fightmetrics.controller;

import com.david.fightmetrics.entity.Fighter;
import com.david.fightmetrics.entity.WeightClass;
import com.david.fightmetrics.service.FighterService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fighters")
public class FighterController {

    private final FighterService fighterService;

    public FighterController(FighterService fighterService) {
        this.fighterService = fighterService;
    }

    @GetMapping
    public String listFighters(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) WeightClass weightClass,
            Model model
    ) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("fighters", fighterService.search(search));
        } else if (weightClass != null) {
            model.addAttribute(
                    "fighters",
                    fighterService.findByWeightClass(weightClass)
            );
        } else {
            model.addAttribute("fighters", fighterService.findAll());
        }

        model.addAttribute("search", search);
        model.addAttribute("selectedWeightClass", weightClass);
        model.addAttribute("weightClasses", WeightClass.values());

        return "fighters/list";
    }

    @GetMapping("/{id}")
    public String showFighter(@PathVariable Long id, Model model) {
        model.addAttribute("fighter", fighterService.findById(id));
        return "fighters/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("fighter", new Fighter());
        model.addAttribute("weightClasses", WeightClass.values());
        return "fighters/form";
    }

    @PostMapping
    public String createFighter(
            @Valid @ModelAttribute("fighter") Fighter fighter,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("weightClasses", WeightClass.values());
            return "fighters/form";
        }

        Fighter savedFighter = fighterService.save(fighter);

        return "redirect:/fighters/" + savedFighter.getId();
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("fighter", fighterService.findById(id));
        model.addAttribute("weightClasses", WeightClass.values());

        return "fighters/form";
    }

    @PostMapping("/{id}")
    public String updateFighter(
            @PathVariable Long id,
            @Valid @ModelAttribute("fighter") Fighter fighter,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("weightClasses", WeightClass.values());
            return "fighters/form";
        }

        fighter.setId(id);
        fighterService.save(fighter);

        return "redirect:/fighters/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteFighter(@PathVariable Long id) {
        fighterService.deleteById(id);

        return "redirect:/fighters";
    }
}
