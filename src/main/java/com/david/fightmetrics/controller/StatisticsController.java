package com.david.fightmetrics.controller;

import com.david.fightmetrics.service.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(
            StatisticsService statisticsService
    ) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public String showStatistics(Model model) {

        model.addAttribute(
                "mostWins",
                statisticsService.findMostWins()
        );

        model.addAttribute(
                "mostKoTkoWins",
                statisticsService.findMostKoTkoWins()
        );

        model.addAttribute(
                "mostSubmissionWins",
                statisticsService.findMostSubmissionWins()
        );

        model.addAttribute(
                "longestWinStreaks",
                statisticsService.findLongestWinStreaks()
        );

        model.addAttribute(
                "bestWinPercentages",
                statisticsService.findBestWinPercentages()
        );

        model.addAttribute(
                "bestFinishPercentages",
                statisticsService.findBestFinishPercentages()
        );

        return "statistics/index";
    }
}
