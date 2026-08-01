package com.david.fightmetrics.service;

import com.david.fightmetrics.entity.Event;
import com.david.fightmetrics.entity.EventStatus;
import com.david.fightmetrics.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findAll() {
        return eventRepository.findAllByOrderByEventDateDesc();
    }

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No existe ningún evento con el id " + id
                        )
                );
    }

    public Event save(Event event) {
        return eventRepository.save(event);
    }

    public void deleteById(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "No existe ningún evento con el id " + id
            );
        }

        eventRepository.deleteById(id);
    }

    public List<Event> search(String query) {
        return eventRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Event> findByStatus(EventStatus status) {
        return eventRepository.findByStatus(status);
    }

    public List<Event> findUpcomingEvents() {
        return eventRepository
                .findByEventDateAfterOrderByEventDateAsc(LocalDate.now());
    }
}
