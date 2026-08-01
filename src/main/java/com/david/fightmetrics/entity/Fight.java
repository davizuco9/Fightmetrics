package com.david.fightmetrics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "fights")
public class Fight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "red_corner_fighter_id", nullable = false)
    private Fighter redCornerFighter;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blue_corner_fighter_id", nullable = false)
    private Fighter blueCornerFighter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    private Fighter winner;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WeightClass weightClass;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private Integer scheduledRounds = 3;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FightStatus status = FightStatus.SCHEDULED;

    @Enumerated(EnumType.STRING)
    private VictoryMethod victoryMethod;

    @Min(1)
    @Max(5)
    private Integer finishRound;

    @Min(0)
    @Max(5)
    private Integer finishMinute;

    @Min(0)
    @Max(59)
    private Integer finishSecond;

    private Boolean mainEvent = false;

    public Fight() {
    }

    public @NotNull Fighter getBlueCornerFighter() {
        return blueCornerFighter;
    }

    public void setBlueCornerFighter(@NotNull Fighter blueCornerFighter) {
        this.blueCornerFighter = blueCornerFighter;
    }

    public @NotNull Event getEvent() {
        return event;
    }

    public void setEvent(@NotNull Event event) {
        this.event = event;
    }

    public @Min(0) @Max(5) Integer getFinishMinute() {
        return finishMinute;
    }

    public void setFinishMinute(@Min(0) @Max(5) Integer finishMinute) {
        this.finishMinute = finishMinute;
    }

    public @Min(1) @Max(5) Integer getFinishRound() {
        return finishRound;
    }

    public void setFinishRound(@Min(1) @Max(5) Integer finishRound) {
        this.finishRound = finishRound;
    }

    public Fighter getWinner() {
        return winner;
    }

    public void setWinner(Fighter winner) {
        this.winner = winner;
    }

    public @NotNull WeightClass getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(@NotNull WeightClass weightClass) {
        this.weightClass = weightClass;
    }

    public VictoryMethod getVictoryMethod() {
        return victoryMethod;
    }

    public void setVictoryMethod(VictoryMethod victoryMethod) {
        this.victoryMethod = victoryMethod;
    }

    public @NotNull FightStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull FightStatus status) {
        this.status = status;
    }

    public @Min(1) @Max(5) Integer getScheduledRounds() {
        return scheduledRounds;
    }

    public void setScheduledRounds(@Min(1) @Max(5) Integer scheduledRounds) {
        this.scheduledRounds = scheduledRounds;
    }

    public @NotNull Fighter getRedCornerFighter() {
        return redCornerFighter;
    }

    public void setRedCornerFighter(@NotNull Fighter redCornerFighter) {
        this.redCornerFighter = redCornerFighter;
    }

    public Boolean getMainEvent() {
        return mainEvent;
    }

    public void setMainEvent(Boolean mainEvent) {
        this.mainEvent = mainEvent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @Min(0) @Max(59) Integer getFinishSecond() {
        return finishSecond;
    }

    public void setFinishSecond(@Min(0) @Max(59) Integer finishSecond) {
        this.finishSecond = finishSecond;
    }
}
