package com.david.fightmetrics.repository;

import com.david.fightmetrics.entity.Event;
import com.david.fightmetrics.entity.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByNameContainingIgnoreCase(String name);

    List<Event> findByStatus(EventStatus status);

    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDate date);

    List<Event> findAllByOrderByEventDateDesc();
}
