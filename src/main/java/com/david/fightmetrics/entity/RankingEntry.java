package com.david.fightmetrics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(
        name = "ranking_entries",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_ranking_position",
                        columnNames = {
                                "organization",
                                "ranking_type",
                                "weight_class",
                                "position"
                        }
                ),
                @UniqueConstraint(
                        name = "uk_ranking_fighter",
                        columnNames = {
                                "organization",
                                "ranking_type",
                                "weight_class",
                                "fighter_id"
                        }
                )
        }
)
public class RankingEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La organización es obligatoria")
    @Column(nullable = false, length = 50)
    private String organization;

    @NotNull(message = "El tipo de ranking es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "ranking_type", nullable = false, length = 30)
    private RankingType rankingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_class", length = 40)
    private WeightClass weightClass;

    @NotNull(message = "El luchador es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "fighter_id",
            nullable = false
    )
    private Fighter fighter;

    @NotNull(message = "La posición es obligatoria")
    @Min(
            value = 1,
            message = "La posición debe ser mayor o igual que 1"
    )
    @Column(nullable = false)
    private Integer position;

    @Column(name = "previous_position")
    private Integer previousPosition;

    @Column(name = "ranking_date", nullable = false)
    private LocalDate rankingDate;

    @Column(nullable = false)
    private Boolean champion = false;

    public RankingEntry() {
    }

    @PrePersist
    public void prePersist() {
        if (rankingDate == null) {
            rankingDate = LocalDate.now();
        }

        if (champion == null) {
            champion = false;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public RankingType getRankingType() {
        return rankingType;
    }

    public void setRankingType(RankingType rankingType) {
        this.rankingType = rankingType;
    }

    public WeightClass getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(WeightClass weightClass) {
        this.weightClass = weightClass;
    }

    public Fighter getFighter() {
        return fighter;
    }

    public void setFighter(Fighter fighter) {
        this.fighter = fighter;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(Integer previousPosition) {
        this.previousPosition = previousPosition;
    }

    public LocalDate getRankingDate() {
        return rankingDate;
    }

    public void setRankingDate(LocalDate rankingDate) {
        this.rankingDate = rankingDate;
    }

    public Boolean getChampion() {
        return champion;
    }

    public void setChampion(Boolean champion) {
        this.champion = champion;
    }
}