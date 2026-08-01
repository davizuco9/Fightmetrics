package com.david.fightmetrics.controller;

import com.david.fightmetrics.entity.Event;
import com.david.fightmetrics.entity.EventStatus;
import com.david.fightmetrics.service.EventService;
import com.david.fightmetrics.service.FightService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final FightService fightService;

    public EventController(
            EventService eventService,
            FightService fightService
    ) {
        this.eventService = eventService;
        this.fightService = fightService;
    }

    @GetMapping
    public String listEvents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) EventStatus status,
            Model model
    ) {
        if (search != null && !search.isBlank()) {
            model.addAttribute("events", eventService.search(search));
        } else if (status != null) {
            model.addAttribute("events", eventService.findByStatus(status));
        } else {
            model.addAttribute("events", eventService.findAll());
        }

        model.addAttribute("search", search);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("eventStatuses", EventStatus.values());

        return "events/list";
    }

    @GetMapping("/{id}")
    public String showEvent(
            @PathVariable Long id,
            Model model
    ) {
        Event event = eventService.findById(id);

        model.addAttribute("event", event);
        model.addAttribute("fights", fightService.findByEvent(event));

        return "events/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("eventStatuses", EventStatus.values());

        return "events/form";
    }

    @PostMapping
    public String createEvent(
            @Valid @ModelAttribute("event") Event event,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventStatuses", EventStatus.values());
            return "events/form";
        }

        Event savedEvent = eventService.save(event);

        return "redirect:/events/" + savedEvent.getId();
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("event", eventService.findById(id));
        model.addAttribute("eventStatuses", EventStatus.values());

        return "events/form";
    }

    @PostMapping("/{id}")
    public String updateEvent(
            @PathVariable Long id,
            @Valid @ModelAttribute("event") Event event,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("eventStatuses", EventStatus.values());
            return "events/form";
        }

        event.setId(id);
        eventService.save(event);

        return "redirect:/events/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteById(id);

        return "redirect:/events";
    }
}