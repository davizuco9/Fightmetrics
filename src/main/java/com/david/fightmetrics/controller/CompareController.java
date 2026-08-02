package com.david.fightmetrics.controller;

import com.david.fightmetrics.entity.Fighter;
import com.david.fightmetrics.service.FightService;
import com.david.fightmetrics.service.FighterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/compare")
public class CompareController {

    private final FighterService fighterService;
    private final FightService fightService;

    public CompareController(
            FighterService fighterService,
            FightService fightService
    ) {
        this.fighterService = fighterService;
        this.fightService = fightService;
    }

    @GetMapping
    public String showSelectionForm(Model model) {
        model.addAttribute(
                "fighters",
                fighterService.findAll()
        );

        return "compare/select";
    }

    @PostMapping
    public String compareFighters(
            @RequestParam Long fighter1Id,
            @RequestParam Long fighter2Id,
            Model model
    ) {
        if (fighter1Id.equals(fighter2Id)) {
            model.addAttribute(
                    "compareError",
                    "Debes seleccionar dos luchadores diferentes"
            );

            model.addAttribute(
                    "fighters",
                    fighterService.findAll()
            );

            model.addAttribute(
                    "selectedFighter1Id",
                    fighter1Id
            );

            model.addAttribute(
                    "selectedFighter2Id",
                    fighter2Id
            );

            return "compare/select";
        }

        return "redirect:/compare/result"
                + "?fighter1Id=" + fighter1Id
                + "&fighter2Id=" + fighter2Id;
    }

    @GetMapping("/result")
    public String showComparison(
            @RequestParam Long fighter1Id,
            @RequestParam Long fighter2Id,
            Model model
    ) {
        if (fighter1Id.equals(fighter2Id)) {
            return "redirect:/compare";
        }

        Fighter fighter1 =
                fighterService.findById(fighter1Id);

        Fighter fighter2 =
                fighterService.findById(fighter2Id);

        model.addAttribute(
                "fighter1",
                fighter1
        );

        model.addAttribute(
                "fighter2",
                fighter2
        );

        model.addAttribute(
                "stats1",
                fightService.calculateStats(fighter1)
        );

        model.addAttribute(
                "stats2",
                fightService.calculateStats(fighter2)
        );

        return "compare/result";
    }
}