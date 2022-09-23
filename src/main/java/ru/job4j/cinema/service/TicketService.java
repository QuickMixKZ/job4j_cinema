package ru.job4j.cinema.service;

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

    public List<Ticket> createTickets(List<Ticket> tickets) {
        return ticketRepository.createTickets(tickets);
    }

    public List<Ticket> findActualTicketsByUser(User user) {
        return ticketRepository.findActualTicketsByUser(user);
    }
}
