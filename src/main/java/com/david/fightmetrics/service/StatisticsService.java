package com.david.fightmetrics.service;

import com.david.fightmetrics.dto.FighterLeaderboardRow;
import com.david.fightmetrics.dto.FighterStats;
import com.david.fightmetrics.entity.Fighter;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;

@Service
public class StatisticsService {

    private static final int DEFAULT_LIMIT = 10;

    private final FighterService fighterService;
    private final FightService fightService;

    public StatisticsService(
            FighterService fighterService,
            FightService fightService
    ) {
        this.fighterService = fighterService;
        this.fightService = fightService;
    }

    public List<FighterLeaderboardRow> findMostWins() {
        return sortByLongValue(
                row -> row.getStats().getWins(),
                DEFAULT_LIMIT,
                false
        );
    }

    public List<FighterLeaderboardRow> findMostKoTkoWins() {
        return sortByLongValue(
                row -> row.getStats().getKoTkoWins(),
                DEFAULT_LIMIT,
                false
        );
    }

    public List<FighterLeaderboardRow> findMostSubmissionWins() {
        return sortByLongValue(
                row -> row.getStats().getSubmissionWins(),
                DEFAULT_LIMIT,
                false
        );
    }

    public List<FighterLeaderboardRow> findLongestWinStreaks() {
        return sortByLongValue(
                row -> row.getStats().getCurrentWinStreak(),
                DEFAULT_LIMIT,
                false
        );
    }

    public List<FighterLeaderboardRow> findBestWinPercentages() {
        return sortByDoubleValue(
                row -> row.getStats().getWinPercentage(),
                DEFAULT_LIMIT,
                true
        );
    }

    public List<FighterLeaderboardRow> findBestFinishPercentages() {
        return sortByDoubleValue(
                row -> row.getStats().getFinishPercentage(),
                DEFAULT_LIMIT,
                true
        );
    }

    private List<FighterLeaderboardRow> buildRows() {
        return fighterService.findAll()
                .stream()
                .map(this::buildRow)
                .toList();
    }

    private FighterLeaderboardRow buildRow(Fighter fighter) {
        FighterStats stats =
                fightService.calculateStats(fighter);

        return new FighterLeaderboardRow(
                fighter,
                stats
        );
    }

    private List<FighterLeaderboardRow> sortByLongValue(
            ToLongFunction<FighterLeaderboardRow> valueExtractor,
            int limit,
            boolean requireFights
    ) {
        return buildRows()
                .stream()
                .filter(row ->
                        !requireFights
                                || row.getStats().getTotalFights() > 0
                )
                .filter(row ->
                        valueExtractor.applyAsLong(row) > 0
                )
                .sorted(
                        Comparator
                                .comparingLong(valueExtractor)
                                .reversed()
                                .thenComparing(
                                        row -> row.getFighter().getLastName(),
                                        String.CASE_INSENSITIVE_ORDER
                                )
                )
                .limit(limit)
                .toList();
    }

    private List<FighterLeaderboardRow> sortByDoubleValue(
            ToDoubleFunction<FighterLeaderboardRow> valueExtractor,
            int limit,
            boolean requireFights
    ) {
        return buildRows()
                .stream()
                .filter(row ->
                        !requireFights
                                || row.getStats().getTotalFights() > 0
                )
                .filter(row ->
                        valueExtractor.applyAsDouble(row) > 0
                )
                .sorted(
                        Comparator
                                .comparingDouble(valueExtractor)
                                .reversed()
                                .thenComparing(
                                        row -> row.getFighter().getLastName(),
                                        String.CASE_INSENSITIVE_ORDER
                                )
                )
                .limit(limit)
                .toList();
    }
}
