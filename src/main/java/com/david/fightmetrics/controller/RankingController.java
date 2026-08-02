package com.david.fightmetrics.controller;

import com.david.fightmetrics.entity.RankingEntry;
import com.david.fightmetrics.entity.RankingType;
import com.david.fightmetrics.entity.WeightClass;
import com.david.fightmetrics.service.FighterService;
import com.david.fightmetrics.service.RankingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/rankings")
public class RankingController {

    private final RankingService rankingService;
    private final FighterService fighterService;

    public RankingController(
            RankingService rankingService,
            FighterService fighterService
    ) {
        this.rankingService = rankingService;
        this.fighterService = fighterService;
    }

    @GetMapping
    public String showRankings(
            @RequestParam(defaultValue = "UFC") String organization,
            @RequestParam(defaultValue = "POUND_FOR_POUND") RankingType rankingType,
            @RequestParam(required = false) WeightClass weightClass,
            Model model
    ) {
        String normalizedOrganization =
                organization == null || organization.isBlank()
                        ? "UFC"
                        : organization.trim().toUpperCase();

        if (rankingType == RankingType.WEIGHT_CLASS) {
            if (weightClass == null) {
                weightClass = WeightClass.LIGHTWEIGHT;
            }

            model.addAttribute(
                    "rankingEntries",
                    rankingService.findByWeightClass(
                            normalizedOrganization,
                            weightClass
                    )
            );
        } else {
            model.addAttribute(
                    "rankingEntries",
                    rankingService.findPoundForPound(
                            normalizedOrganization
                    )
            );
        }

        model.addAttribute(
                "organization",
                normalizedOrganization
        );

        model.addAttribute(
                "selectedRankingType",
                rankingType
        );

        model.addAttribute(
                "selectedWeightClass",
                weightClass
        );

        model.addAttribute(
                "rankingTypes",
                RankingType.values()
        );

        model.addAttribute(
                "weightClasses",
                WeightClass.values()
        );

        return "rankings/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        RankingEntry rankingEntry = new RankingEntry();
        rankingEntry.setOrganization("UFC");
        rankingEntry.setRankingType(RankingType.POUND_FOR_POUND);
        rankingEntry.setChampion(false);

        prepareForm(model, rankingEntry);

        return "rankings/form";
    }

    @PostMapping
    public String createRankingEntry(
            @Valid @ModelAttribute("rankingEntry") RankingEntry rankingEntry,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            prepareForm(model, rankingEntry);
            return "rankings/form";
        }

        try {
            RankingEntry savedEntry =
                    rankingService.save(rankingEntry);

            return buildRankingRedirect(savedEntry);
        } catch (IllegalArgumentException exception) {
            model.addAttribute(
                    "rankingError",
                    exception.getMessage()
            );

            prepareForm(model, rankingEntry);

            return "rankings/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Model model
    ) {
        prepareForm(
                model,
                rankingService.findById(id)
        );

        return "rankings/form";
    }

    @PostMapping("/{id}")
    public String updateRankingEntry(
            @PathVariable Long id,
            @Valid @ModelAttribute("rankingEntry") RankingEntry rankingEntry,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            prepareForm(model, rankingEntry);
            return "rankings/form";
        }

        try {
            rankingEntry.setId(id);

            RankingEntry savedEntry =
                    rankingService.save(rankingEntry);

            return buildRankingRedirect(savedEntry);
        } catch (IllegalArgumentException exception) {
            model.addAttribute(
                    "rankingError",
                    exception.getMessage()
            );

            prepareForm(model, rankingEntry);

            return "rankings/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteRankingEntry(
            @PathVariable Long id
    ) {
        RankingEntry rankingEntry =
                rankingService.findById(id);

        String redirect =
                buildRankingRedirect(rankingEntry);

        rankingService.deleteById(id);

        return redirect;
    }

    private void prepareForm(
            Model model,
            RankingEntry rankingEntry
    ) {
        model.addAttribute(
                "rankingEntry",
                rankingEntry
        );

        model.addAttribute(
                "fighters",
                fighterService.findAll()
        );

        model.addAttribute(
                "rankingTypes",
                RankingType.values()
        );

        model.addAttribute(
                "weightClasses",
                WeightClass.values()
        );
    }

    private String buildRankingRedirect(
            RankingEntry rankingEntry
    ) {
        StringBuilder redirect =
                new StringBuilder(
                        "redirect:/rankings?organization="
                );

        redirect.append(
                rankingEntry.getOrganization()
        );

        redirect.append(
                "&rankingType="
        );

        redirect.append(
                rankingEntry.getRankingType()
        );

        if (rankingEntry.getRankingType()
                == RankingType.WEIGHT_CLASS
                && rankingEntry.getWeightClass() != null) {

            redirect.append(
                    "&weightClass="
            );

            redirect.append(
                    rankingEntry.getWeightClass()
            );
        }

        return redirect.toString();
    }
}
