package com.david.fightmetrics.controller;

import com.david.fightmetrics.entity.*;
import com.david.fightmetrics.service.EventService;
import com.david.fightmetrics.service.FightService;
import com.david.fightmetrics.service.FighterService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fights")
public class FightController {

    private final FightService fightService;
    private final EventService eventService;
    private final FighterService fighterService;

    public FightController(
            FightService fightService,
            EventService eventService,
            FighterService fighterService
    ) {
        this.fightService = fightService;
        this.eventService = eventService;
        this.fighterService = fighterService;
    }

    @GetMapping("/new")
    public String showCreateForm(
            @RequestParam Long eventId,
            Model model
    ) {
        Fight fight = new Fight();
        fight.setEvent(eventService.findById(eventId));

        prepareForm(model, fight);

        return "fights/form";
    }

    @PostMapping
    public String createFight(
            @Valid @ModelAttribute("fight") Fight fight,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            prepareForm(model, fight);
            return "fights/form";
        }

        try {
            Fight savedFight = fightService.save(fight);
            return "redirect:/events/" + savedFight.getEvent().getId();
        } catch (IllegalArgumentException exception) {
            model.addAttribute("fightError", exception.getMessage());
            prepareForm(model, fight);
            return "fights/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Model model
    ) {
        prepareForm(model, fightService.findById(id));
        return "fights/form";
    }

    @PostMapping("/{id}")
    public String updateFight(
            @PathVariable Long id,
            @Valid @ModelAttribute("fight") Fight fight,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            prepareForm(model, fight);
            return "fights/form";
        }

        try {
            fight.setId(id);
            Fight savedFight = fightService.save(fight);
            return "redirect:/events/" + savedFight.getEvent().getId();
        } catch (IllegalArgumentException exception) {
            model.addAttribute("fightError", exception.getMessage());
            prepareForm(model, fight);
            return "fights/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteFight(@PathVariable Long id) {
        Fight fight = fightService.findById(id);
        Long eventId = fight.getEvent().getId();

        fightService.deleteById(id);

        return "redirect:/events/" + eventId;
    }

    private void prepareForm(Model model, Fight fight) {
        model.addAttribute("fight", fight);
        model.addAttribute("events", eventService.findAll());
        model.addAttribute("fighters", fighterService.findAll());
        model.addAttribute("weightClasses", WeightClass.values());
        model.addAttribute("fightStatuses", FightStatus.values());
        model.addAttribute("victoryMethods", VictoryMethod.values());
    }
}
