package ru.job4j.cinema.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public ResponseEntity<Void> createTickets(Ticket[] tickets) {
        return new ResponseEntity<>(
                ticketRepository.createTickets(tickets) ? HttpStatus.ACCEPTED : HttpStatus.BAD_REQUEST
        );
    }

    public List<Ticket> findActualTicketsByUser(User user) {
        return ticketRepository.findActualTicketsByUser(user);
    }
}
