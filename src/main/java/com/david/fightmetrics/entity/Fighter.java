package com.david.fightmetrics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "fighters")
public class Fighter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    private String nickname;

    private LocalDate birthDate;

    @NotBlank
    @Column(nullable = false)
    private String nationality;

    private Double heightCm;

    private Double reachCm;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private WeightClass weightClass;

    @Min(0)
    private Integer wins = 0;

    @Min(0)
    private Integer losses = 0;

    @Min(0)
    private Integer draws = 0;

    @Min(0)
    private Integer knockoutWins = 0;

    @Min(0)
    private Integer submissionWins = 0;

    @Min(0)
    private Integer decisionWins = 0;

    private String imageUrl;

    private Boolean active = true;

    public Fighter() {
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public @Min(0) Integer getDecisionWins() {
        return decisionWins;
    }

    public void setDecisionWins(@Min(0) Integer decisionWins) {
        this.decisionWins = decisionWins;
    }

    public @Min(0) Integer getDraws() {
        return draws;
    }

    public void setDraws(@Min(0) Integer draws) {
        this.draws = draws;
    }

    public @NotBlank String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank String firstName) {
        this.firstName = firstName;
    }

    public Double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Double heightCm) {
        this.heightCm = heightCm;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @Min(0) Integer getKnockoutWins() {
        return knockoutWins;
    }

    public void setKnockoutWins(@Min(0) Integer knockoutWins) {
        this.knockoutWins = knockoutWins;
    }

    public @NotBlank String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank String lastName) {
        this.lastName = lastName;
    }

    public @Min(0) Integer getLosses() {
        return losses;
    }

    public void setLosses(@Min(0) Integer losses) {
        this.losses = losses;
    }

    public @NotBlank String getNationality() {
        return nationality;
    }

    public void setNationality(@NotBlank String nationality) {
        this.nationality = nationality;
    }

    public @Min(0) Integer getWins() {
        return wins;
    }

    public void setWins(@Min(0) Integer wins) {
        this.wins = wins;
    }

    public @NotNull WeightClass getWeightClass() {
        return weightClass;
    }

    public void setWeightClass(@NotNull WeightClass weightClass) {
        this.weightClass = weightClass;
    }

    public @Min(0) Integer getSubmissionWins() {
        return submissionWins;
    }

    public void setSubmissionWins(@Min(0) Integer submissionWins) {
        this.submissionWins = submissionWins;
    }

    public Double getReachCm() {
        return reachCm;
    }

    public void setReachCm(Double reachCm) {
        this.reachCm = reachCm;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
