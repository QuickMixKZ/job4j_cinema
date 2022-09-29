package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.CinemaHall;
import ru.job4j.cinema.repository.CinemaHallRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CinemaHallService {

    private final CinemaHallRepository cinemaHallRepository;

    public CinemaHallService(CinemaHallRepository cinemaHallRepository) {
        this.cinemaHallRepository = cinemaHallRepository;
    }

    public CinemaHall add(CinemaHall cinemaHall) {
        checkExistingCinemaHallByName(cinemaHall);
        return cinemaHallRepository.add(cinemaHall);
    }

    public List<CinemaHall> findAll() {
        return cinemaHallRepository.findAll();
    }

    public CinemaHall findById(int id) {
        Optional<CinemaHall> result = cinemaHallRepository.findById(id);
        if (result.isEmpty()) {
            throw new NoSuchElementException(String.format("Cinema hall with ID:%d not found.", id));
        }
        return result.get();
    }

    public CinemaHall findByName(String name) {
        Optional<CinemaHall> result = cinemaHallRepository.findByName(name);
        if (result.isEmpty()) {
            throw new NoSuchElementException(String.format("Cinema hall with name \"%s\" not found.", name));
        }
        return result.get();
    }

    public boolean update(CinemaHall cinemaHall) {
        findById(cinemaHall.getId());
        checkExistingCinemaHallByName(cinemaHall);
        return cinemaHallRepository.update(cinemaHall);
    }

    public boolean delete(CinemaHall cinemaHall) {
        findById(cinemaHall.getId());
        return cinemaHallRepository.delete(cinemaHall);
    }

    public void deleteAll() {
        cinemaHallRepository.deleteAll();
    }

    private void checkExistingCinemaHallByName(CinemaHall cinemaHall) {
        Optional<CinemaHall> cinemaHallWithSameName = cinemaHallRepository.findByName(cinemaHall.getName());
        if (cinemaHallWithSameName.isPresent() && cinemaHallWithSameName.get().getId() != cinemaHall.getId()) {
            throw new IllegalArgumentException(
                    String.format(
                            "Unable to update Cinema hall. "
                                    + "Cinema hall with name \"%s\" already exists.", cinemaHall.getName())
            );
        }
    }
}
