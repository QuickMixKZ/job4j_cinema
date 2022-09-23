package ru.job4j.cinema.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.service.UserService;

import java.util.List;

@Controller
public class TicketController {

    private final SessionService sessionService;
    private final TicketService ticketService;
    private final UserService userService;

    public TicketController(SessionService sessionService,
                            TicketService ticketService,
                            UserService userService) {
        this.sessionService = sessionService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @PostMapping("/movies-today/buyTickets")
    public ResponseEntity<List<Ticket>> buyTickets(@RequestBody List<Ticket> tickets) {
        if (tickets.size() == 0) {
            throw new IllegalArgumentException("No tickets selected");
        }
        Session session = sessionService.findById(tickets.get(0).getSession().getId());
        User currentUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        tickets.forEach(ticket -> {
            ticket.setUser(currentUser);
            ticket.setSession(session);
        });
        tickets = ticketService.createTickets(tickets);
        boolean accepted = true;
        for (Ticket ticket : tickets) {
            accepted = ticket.getId() != 0;
            if (!accepted) {
                break;
            }
        }
        return new ResponseEntity<>(
                tickets,
                accepted ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST
        );
    }
}
