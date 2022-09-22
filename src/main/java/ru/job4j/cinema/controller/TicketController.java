package ru.job4j.cinema.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.CinemaHallService;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.service.UserService;

import java.util.Arrays;

@Controller
public class TicketController {

    private final CinemaHallService cinemaHallService;
    private final SessionService sessionService;
    private final TicketService ticketService;
    private final UserService userService;

    public TicketController(CinemaHallService cinemaHallService,
                            SessionService sessionService,
                            TicketService ticketService,
                            UserService userService) {
        this.cinemaHallService = cinemaHallService;
        this.sessionService = sessionService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @PostMapping("/movies-today/buyTickets")
    public ResponseEntity<Void> buyTickets(@RequestBody Ticket[] tickets) {
        if (tickets.length == 0) {
            throw new IllegalArgumentException("No tickets selected");
        }
        Session session = sessionService.findById(tickets[0].getSession().getId());
        User currentUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Arrays.stream(tickets).forEach(ticket -> {
            ticket.setUser(currentUser);
            ticket.setSession(session);
        });
        return ticketService.createTickets(tickets);
    }
}
