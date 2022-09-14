package ru.job4j.cinema.service;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.repository.GenreRepository;
import ru.job4j.cinema.repository.MovieRepository;

import java.util.List;
import java.util.Set;

public class MovieServiceTest {

    private final BasicDataSource pool = new Main().loadPool();
    private final GenreService genreService = new GenreService(new GenreRepository(pool));
    private final MovieService movieService = new MovieService(new MovieRepository(new Main().loadPool()));

    @After
    public void wipeTables() {
        movieService.deleteAll();
        genreService.deleteAll();
    }

    @Test
    public void whenAddTwoMoviesThenFindById() {
        Genre addedGenre = genreService.add(new Genre("Триллер"));
        Genre addedGenre1 = genreService.add(new Genre("Комедия"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Movie newMovie = new Movie("Первый фильм", "Описание первого фильма", 2020, null, 90);
        newMovie.setGenres(Set.of(addedGenre, addedGenre1, addedGenre2));
        Movie newMovie1 = new Movie("Второй фильм", "Описание второго фильма", 2020, null, 90);
        newMovie1.setGenres(Set.of(addedGenre1, addedGenre2));
        Movie addedMovie = movieService.add(newMovie);
        Movie addedMovie1 = movieService.add(newMovie1);
        Assertions.assertEquals(addedMovie, movieService.findById(addedMovie.getId()));
        Assertions.assertEquals(addedMovie1, movieService.findById(addedMovie1.getId()));
    }

    @Test
    public void whenAddTwoMoviesThenFindAll() {
        Genre addedGenre = genreService.add(new Genre("Триллер"));
        Genre addedGenre1 = genreService.add(new Genre("Комедия"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Movie newMovie = new Movie("Первый фильм", "Описание первого фильма", 2020, null, 90);
        newMovie.setGenres(Set.of(addedGenre, addedGenre1, addedGenre2));
        Movie newMovie1 = new Movie("Второй фильм", "Описание второго фильма", 2020, null, 90);
        newMovie1.setGenres(Set.of(addedGenre1, addedGenre2));
        Movie addedMovie = movieService.add(newMovie);
        Movie addedMovie1 = movieService.add(newMovie1);
        List<Movie> allMovies = movieService.findAll();
        Assertions.assertEquals(2, allMovies.size());
        Assertions.assertEquals(List.of(addedMovie, addedMovie1), allMovies);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddTwoMoviesWithSameNameAndYearThenException() {
        Genre addedGenre = genreService.add(new Genre("Триллер"));
        Genre addedGenre1 = genreService.add(new Genre("Комедия"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Movie newMovie = new Movie("Первый фильм", "Описание первого фильма", 2020, null, 90);
        newMovie.setGenres(Set.of(addedGenre, addedGenre1, addedGenre2));
        Movie addedMovie = movieService.add(newMovie);
        Movie addedMovie1 = movieService.add(newMovie);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddTwoMoviesAndUpdateOneToSameNameAndYearWithFirstThenException() {
        Genre addedGenre = genreService.add(new Genre("Триллер"));
        Genre addedGenre1 = genreService.add(new Genre("Комедия"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Movie newMovie = new Movie("Первый фильм", "Описание первого фильма", 2020, null, 90);
        newMovie.setGenres(Set.of(addedGenre, addedGenre1, addedGenre2));
        Movie newMovie1 = new Movie("Второй фильм", "Описание второго фильма", 2020, null, 90);
        newMovie1.setGenres(Set.of(addedGenre1, addedGenre2));
        Movie addedMovie = movieService.add(newMovie);
        Movie addedMovie1 = movieService.add(newMovie1);
        addedMovie1.setName("Первый фильм");
        movieService.update(addedMovie1);
    }

    @Test
    public void whenAddTwoMoviesAndUpdateOneToSameNameAndOtherYearWithFirst() {
        Genre addedGenre = genreService.add(new Genre("Триллер"));
        Genre addedGenre1 = genreService.add(new Genre("Комедия"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Movie newMovie = new Movie("Первый фильм", "Описание первого фильма", 2020, null, 90);
        newMovie.setGenres(Set.of(addedGenre, addedGenre1, addedGenre2));
        Movie newMovie1 = new Movie("Второй фильм", "Описание второго фильма", 2020, null, 90);
        newMovie1.setGenres(Set.of(addedGenre1, addedGenre2));
        Movie addedMovie = movieService.add(newMovie);
        Movie addedMovie1 = movieService.add(newMovie1);
        addedMovie1.setName("Первый фильм");
        addedMovie1.setYear(2021);
        Assertions.assertTrue(movieService.update(addedMovie1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddTwoMoviesAndUpdateWithWrongId() {
        Genre addedGenre = genreService.add(new Genre("Триллер"));
        Genre addedGenre1 = genreService.add(new Genre("Комедия"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Movie newMovie = new Movie("Первый фильм", "Описание первого фильма", 2020, null, 90);
        newMovie.setGenres(Set.of(addedGenre, addedGenre1, addedGenre2));
        Movie newMovie1 = new Movie("Второй фильм", "Описание второго фильма", 2020, null, 90);
        newMovie1.setGenres(Set.of(addedGenre1, addedGenre2));
        Movie addedMovie = movieService.add(newMovie);
        Movie addedMovie1 = movieService.add(newMovie1);
        addedMovie1.setName("Первый фильм");
        addedMovie1.setYear(2021);
        addedMovie1.setId(0);
        Assertions.assertTrue(movieService.update(addedMovie1));
    }

    @Test
    public void whenAddTwoMoviesThenDeleteBothById() {
        Genre addedGenre = genreService.add(new Genre("Триллер"));
        Genre addedGenre1 = genreService.add(new Genre("Комедия"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Movie newMovie = new Movie("Первый фильм", "Описание первого фильма", 2020, null, 90);
        newMovie.setGenres(Set.of(addedGenre, addedGenre1, addedGenre2));
        Movie newMovie1 = new Movie("Второй фильм", "Описание второго фильма", 2020, null, 90);
        newMovie1.setGenres(Set.of(addedGenre1, addedGenre2));
        Movie addedMovie = movieService.add(newMovie);
        Movie addedMovie1 = movieService.add(newMovie1);
        Assertions.assertTrue(movieService.delete(addedMovie));
        Assertions.assertTrue(movieService.delete(addedMovie1));
        Assertions.assertEquals(0, movieService.findAll().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddTwoMoviesThenDeleteWithWrongIdThenException() {
        Genre addedGenre = genreService.add(new Genre("Триллер"));
        Genre addedGenre1 = genreService.add(new Genre("Комедия"));
        Genre addedGenre2 = genreService.add(new Genre("Драма"));
        Movie newMovie = new Movie("Первый фильм", "Описание первого фильма", 2020, null, 90);
        newMovie.setGenres(Set.of(addedGenre, addedGenre1, addedGenre2));
        Movie newMovie1 = new Movie("Второй фильм", "Описание второго фильма", 2020, null, 90);
        newMovie1.setGenres(Set.of(addedGenre1, addedGenre2));
        Movie addedMovie = movieService.add(newMovie);
        Movie addedMovie1 = movieService.add(newMovie1);
        addedMovie.setId(0);
        movieService.delete(addedMovie);
    }



}