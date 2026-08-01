package com.david.fightmetrics.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String organization;

    @NotNull
    @Column(nullable = false)
    private LocalDate eventDate;

    @NotBlank
    @Column(nullable = false)
    private String city;

    @NotBlank
    @Column(nullable = false)
    private String country;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private EventStatus status = EventStatus.SCHEDULED;

    public Event() {
    }

    public @NotBlank String getCity() {
        return city;
    }

    public void setCity(@NotBlank String city) {
        this.city = city;
    }

    public @NotBlank String getCountry() {
        return country;
    }

    public void setCountry(@NotBlank String country) {
        this.country = country;
    }

    public @NotNull LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(@NotNull LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getName() {
        return name;
    }

    public void setName(@NotBlank String name) {
        this.name = name;
    }

    public @NotBlank String getOrganization() {
        return organization;
    }

    public void setOrganization(@NotBlank String organization) {
        this.organization = organization;
    }

    public @NotNull EventStatus getStatus() {
        return status;
    }

    public void setStatus(@NotNull EventStatus status) {
        this.status = status;
    }
}
