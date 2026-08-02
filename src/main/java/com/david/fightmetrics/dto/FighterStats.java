package com.david.fightmetrics.dto;

public class FighterStats {

    private long totalFights;
    private long wins;
    private long losses;
    private long draws;
    private long koTkoWins;
    private long submissionWins;
    private long decisionWins;
    private long otherWins;
    private long currentWinStreak;
    private double winPercentage;
    private double finishPercentage;

    public FighterStats() {
    }

    public FighterStats(
            long totalFights,
            long wins,
            long losses,
            long draws,
            long koTkoWins,
            long submissionWins,
            long decisionWins,
            long otherWins,
            long currentWinStreak,
            double winPercentage,
            double finishPercentage
    ) {
        this.totalFights = totalFights;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        this.koTkoWins = koTkoWins;
        this.submissionWins = submissionWins;
        this.decisionWins = decisionWins;
        this.otherWins = otherWins;
        this.currentWinStreak = currentWinStreak;
        this.winPercentage = winPercentage;
        this.finishPercentage = finishPercentage;
    }

    public long getTotalFights() {
        return totalFights;
    }

    public void setTotalFights(long totalFights) {
        this.totalFights = totalFights;
    }

    public long getWins() {
        return wins;
    }

    public void setWins(long wins) {
        this.wins = wins;
    }

    public long getLosses() {
        return losses;
    }

    public void setLosses(long losses) {
        this.losses = losses;
    }

    public long getDraws() {
        return draws;
    }

    public void setDraws(long draws) {
        this.draws = draws;
    }

    public long getKoTkoWins() {
        return koTkoWins;
    }

    public void setKoTkoWins(long koTkoWins) {
        this.koTkoWins = koTkoWins;
    }

    public long getSubmissionWins() {
        return submissionWins;
    }

    public void setSubmissionWins(long submissionWins) {
        this.submissionWins = submissionWins;
    }

    public long getDecisionWins() {
        return decisionWins;
    }

    public void setDecisionWins(long decisionWins) {
        this.decisionWins = decisionWins;
    }

    public long getOtherWins() {
        return otherWins;
    }

    public void setOtherWins(long otherWins) {
        this.otherWins = otherWins;
    }

    public long getCurrentWinStreak() {
        return currentWinStreak;
    }

    public void setCurrentWinStreak(long currentWinStreak) {
        this.currentWinStreak = currentWinStreak;
    }

    public double getWinPercentage() {
        return winPercentage;
    }

    public void setWinPercentage(double winPercentage) {
        this.winPercentage = winPercentage;
    }

    public double getFinishPercentage() {
        return finishPercentage;
    }

    public void setFinishPercentage(double finishPercentage) {
        this.finishPercentage = finishPercentage;
    }
}
