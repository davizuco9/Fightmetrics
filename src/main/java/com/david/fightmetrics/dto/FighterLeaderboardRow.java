package com.david.fightmetrics.dto;

import com.david.fightmetrics.entity.Fighter;

public class FighterLeaderboardRow {

    private final Fighter fighter;
    private final FighterStats stats;

    public FighterLeaderboardRow(
            Fighter fighter,
            FighterStats stats
    ) {
        this.fighter = fighter;
        this.stats = stats;
    }

    public Fighter getFighter() {
        return fighter;
    }

    public FighterStats getStats() {
        return stats;
    }
}