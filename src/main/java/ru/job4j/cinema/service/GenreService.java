package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    public Genre findById(int id) {
        Optional<Genre> result = genreRepository.findById(id);
        if (result.isEmpty()) {
            throw new NoSuchElementException(String.format("Genre with id: %d not found.", id));
        }
        return result.get();
    }

    public Genre findByName(String name) {
        Optional<Genre> result = genreRepository.findByName(name);
        if (result.isEmpty()) {
            throw new NoSuchElementException(String.format("Genre with name \"%s\" not found.", name));
        }
        return result.get();
    }

    public Genre add(Genre genre) {
        Optional<Genre> genreDb = genreRepository.findByName(genre.getName());
        if (genreDb.isPresent()) {
            throw new NoSuchElementException(String.format("Genre with name \"%s\" already exists.", genre.getName()));
        }
        return genreRepository.add(genre);
    }

    public boolean update(Genre genre) {
        Genre genreDb = findById(genre.getId());
        return genreRepository.update(genre);
    }

    public boolean delete(Genre genre) {
        Genre genreDb = findById(genre.getId());
        return genreRepository.delete(genre);
    }

    public void deleteAll() {
        genreRepository.deleteAll();
    }
}
