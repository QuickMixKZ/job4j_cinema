package ru.job4j.cinema.service;

import org.junit.After;
import org.junit.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GenreServiceTest {

    private final GenreRepository genreRepository = new GenreRepository(new Main().loadPool());

    @After
    public void wipeTable() {
        GenreService genreService = new GenreService(genreRepository);
        genreService.deleteAll();
    }

    @Test
    public void whenAddedOneThenFindAll() {
        GenreService genreService = new GenreService(genreRepository);
        Genre addedGenre = genreService.add(new Genre("Триллер"));
        List<Genre> foundAll = genreService.findAll();
        assertEquals(foundAll.size(), 1);
        assertEquals(List.of(addedGenre), foundAll);
    }

    @Test
    public void whenAddedThreeThenFindAll() {
        GenreService genreService = new GenreService(genreRepository);
        Genre addedGenre1 = genreService.add(new Genre("Триллер"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Genre addedGenre3 = genreService.add(new Genre("Комедия"));
        List<Genre> foundAll = genreService.findAll();
        assertEquals(3, foundAll.size());
        assertEquals(List.of(addedGenre1, addedGenre2, addedGenre3), foundAll);
    }

    @Test(expected = NoSuchElementException.class)
    public void whenAddTwoWithSameName() {
        GenreService genreService = new GenreService(genreRepository);
        Genre addedGenre = genreService.add(new Genre("Триллер"));
        Genre addedGenre1 = genreService.add(new Genre("Триллер"));
    }

    @Test
    public void whenAddThreeThenFindById() {
        GenreService genreService = new GenreService(genreRepository);
        Genre addedGenre1 = genreService.add(new Genre("Триллер"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Genre addedGenre3 = genreService.add(new Genre("Комедия"));
        assertEquals(addedGenre1, genreService.findById(addedGenre1.getId()));
        assertEquals(addedGenre2, genreService.findById(addedGenre2.getId()));
        assertEquals(addedGenre3, genreService.findById(addedGenre3.getId()));
    }

    @Test
    public void whenAddThreeThenFindByName() {
        GenreService genreService = new GenreService(genreRepository);
        Genre addedGenre1 = genreService.add(new Genre("Триллер"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Genre addedGenre3 = genreService.add(new Genre("Комедия"));
        assertEquals(addedGenre1, genreService.findByName(addedGenre1.getName()));
        assertEquals(addedGenre2, genreService.findByName(addedGenre2.getName()));
        assertEquals(addedGenre3, genreService.findByName(addedGenre3.getName()));
    }

    @Test
    public void whenAddThreeThenUpdateOne() {
        GenreService genreService = new GenreService(genreRepository);
        Genre addedGenre1 = genreService.add(new Genre("Триллер"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Genre addedGenre3 = genreService.add(new Genre("Комедия"));
        addedGenre1.setName("Ужасы");
        assertTrue(genreService.update(addedGenre1));
        assertEquals(addedGenre1, genreService.findByName(addedGenre1.getName()));
    }

    @Test(expected = NoSuchElementException.class)
    public void whenAddThreeThenUpdateWithWrongId() {
        GenreService genreService = new GenreService(genreRepository);
        Genre addedGenre1 = genreService.add(new Genre("Триллер"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Genre addedGenre3 = genreService.add(new Genre("Комедия"));
        addedGenre1.setName("Ужасы");
        addedGenre1.setId(4);
        genreService.update(addedGenre1);
        assertEquals(addedGenre1, genreService.findByName(addedGenre1.getName()));
    }

    @Test
    public void whenAddThreeThenDeleteOne() {
        GenreService genreService = new GenreService(genreRepository);
        Genre addedGenre1 = genreService.add(new Genre("Триллер"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Genre addedGenre3 = genreService.add(new Genre("Комедия"));
        assertTrue(genreService.delete(addedGenre1));
        assertEquals(List.of(addedGenre2, addedGenre3), genreService.findAll());
    }

    @Test(expected = NoSuchElementException.class)
    public void whenAddThreeThenDeleteWithWrongId() {
        GenreService genreService = new GenreService(genreRepository);
        Genre addedGenre1 = genreService.add(new Genre("Триллер"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Genre addedGenre3 = genreService.add(new Genre("Комедия"));
        addedGenre1.setId(4);
        genreService.delete(addedGenre1);
    }

}