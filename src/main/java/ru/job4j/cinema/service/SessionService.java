package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session add(Session session) {
        if (session.getDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Session date may not be less then now.");
        }
        return sessionRepository.add(session);
    }

    public List<Session> findAll() {
        return sessionRepository.findAll();
    }

    public Session findById(int id) {
        Optional<Session> result = sessionRepository.findById(id);
        if (result.isEmpty()) {
            throw new NoSuchElementException(String.format("Session with ID:%d not found.", id));
        }
        return result.get();
    }

    public boolean update(Session session) {
        findById(session.getId());
        return sessionRepository.update(session);
    }

    public boolean delete(Session session) {
        findById(session.getId());
        return sessionRepository.delete(session);
    }

    public List<Movie> findTodayMovies() {
        return sessionRepository.findTodayMovies();
    }

    public List<Session> findByMovieId(int movieId) {
        return sessionRepository.findByMovieId(movieId);
    }

    public List<Session> findTodayByMovieId(int movieId) {
        return sessionRepository.findTodayByMovieId(movieId);
    }
    public boolean[][] findAvailableSeats(int sessionId) {
        return sessionRepository.findSeatsBySessionId(sessionId);
    }

    public void deleteAll() {
        sessionRepository.deleteAll();
    }
}
