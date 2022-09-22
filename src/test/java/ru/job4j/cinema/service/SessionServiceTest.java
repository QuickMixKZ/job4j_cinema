package ru.job4j.cinema.service;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.CinemaHall;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.CinemaHallRepository;
import ru.job4j.cinema.repository.GenreRepository;
import ru.job4j.cinema.repository.MovieRepository;
import ru.job4j.cinema.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class SessionServiceTest {

    private final BasicDataSource pool = new Main().loadPool();
    private final SessionService sessionService =
            new SessionService(new SessionRepository(pool, new MovieRepository(pool)));
    private final MovieService movieService =
            new MovieService(new MovieRepository(pool));
    private final CinemaHallService cinemaHallService =
            new CinemaHallService(new CinemaHallRepository(pool));
    private final GenreService genreService =
            new GenreService(new GenreRepository(pool));

    @After
    public void wipeTables() {
        sessionService.deleteAll();
        movieService.deleteAll();
        genreService.deleteAll();
        cinemaHallService.deleteAll();
    }

    @Test
    public void whenAddOneThenFindAll() {
        Genre comedyGenre = genreService.add(new Genre("Комедия"));
        Genre dramaGenre = genreService.add(new Genre("Драма"));
        Movie newMovie = new Movie("Один дома",
                "Краткое описание фильма",
                2000,
                null,
                90
        );
        newMovie.setGenres(Set.of(comedyGenre, dramaGenre));
        Movie addedMovie = movieService.add(newMovie);
        CinemaHall addedCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Малый зал",
                        5,
                        10
                )
        );
        Session addedSession = sessionService.add(
                new Session(
                        addedMovie,
                        addedCinemaHall,
                        LocalDateTime.now().plusSeconds(1)
                )
        );
        Assert.assertEquals(List.of(addedSession), sessionService.findAll());
    }

    @Test
    public void whenAddTwoThenFindAll() {
        Genre comedyGenre = genreService.add(new Genre("Комедия"));
        Genre dramaGenre = genreService.add(new Genre("Драма"));
        Movie firstMovie = new Movie("Один дома",
                "Краткое описание фильма",
                2000,
                null,
                90
        );
        firstMovie.setGenres(Set.of(comedyGenre, dramaGenre));
        Movie addedFirstMovie = movieService.add(firstMovie);
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Малый зал",
                        5,
                        10
                )
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Большой зал",
                        10,
                        10
                )
        );
        Session addedFirstSession = sessionService.add(
                new Session(
                        addedFirstMovie,
                        smallCinemaHall,
                        LocalDateTime.now().plusMinutes(1)
                )
        );
        Movie secondMovie = new Movie("Один дома 2",
                "Краткое описание второго фильма",
                2002,
                null,
                120
        );
        secondMovie.setGenres(Set.of(comedyGenre));
        Movie addedSecondMovie = movieService.add(secondMovie);
        Session addedSecondSession = sessionService.add(
                new Session(
                        addedSecondMovie,
                        largeCinemaHall,
                        LocalDateTime.now().plusMinutes(1)
                )
        );
        Assert.assertEquals(List.of(addedFirstSession, addedSecondSession), sessionService.findAll());
    }

    @Test
    public void whenAddTwoThenFindById() {
        Genre comedyGenre = genreService.add(new Genre("Комедия"));
        Genre dramaGenre = genreService.add(new Genre("Драма"));
        Movie firstMovie = new Movie("Один дома",
                "Краткое описание фильма",
                2000,
                null,
                90
        );
        firstMovie.setGenres(Set.of(comedyGenre, dramaGenre));
        Movie addedFirstMovie = movieService.add(firstMovie);
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Малый зал",
                        5,
                        10
                )
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Большой зал",
                        10,
                        10
                )
        );
        Session addedFirstSession = sessionService.add(
                new Session(
                        addedFirstMovie,
                        smallCinemaHall,
                        LocalDateTime.now().plusMinutes(1)
                )
        );
        Movie secondMovie = new Movie("Один дома 2",
                "Краткое описание второго фильма",
                2002,
                null,
                120
        );
        secondMovie.setGenres(Set.of(comedyGenre));
        Movie addedSecondMovie = movieService.add(secondMovie);
        Session addedSecondSession = sessionService.add(
                new Session(
                        addedSecondMovie,
                        largeCinemaHall,
                        LocalDateTime.now().plusMinutes(1)
                )
        );
        Assert.assertEquals(addedFirstSession, sessionService.findById(addedFirstSession.getId()));
        Assert.assertEquals(addedSecondSession, sessionService.findById(addedSecondSession.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddTwoThenFindByWrongId() {
        Genre comedyGenre = genreService.add(new Genre("Комедия"));
        Genre dramaGenre = genreService.add(new Genre("Драма"));
        Movie firstMovie = new Movie("Один дома",
                "Краткое описание фильма",
                2000,
                null,
                90
        );
        firstMovie.setGenres(Set.of(comedyGenre, dramaGenre));
        Movie addedFirstMovie = movieService.add(firstMovie);
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Малый зал",
                        5,
                        10
                )
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Большой зал",
                        10,
                        10
                )
        );
        Session addedFirstSession = sessionService.add(
                new Session(
                        addedFirstMovie,
                        smallCinemaHall,
                        LocalDateTime.now().plusMinutes(1)
                )
        );
        Movie secondMovie = new Movie("Один дома 2",
                "Краткое описание второго фильма",
                2002,
                null,
                120
        );
        secondMovie.setGenres(Set.of(comedyGenre));
        Movie addedSecondMovie = movieService.add(secondMovie);
        Session addedSecondSession = sessionService.add(
                new Session(
                        addedSecondMovie,
                        largeCinemaHall,
                        LocalDateTime.now().plusMinutes(1)
                )
        );
        sessionService.findById(-1);
    }

    @Test
    public void whenAddOneThenUpdateAndFindById() {
        Genre comedyGenre = genreService.add(new Genre("Комедия"));
        Genre dramaGenre = genreService.add(new Genre("Драма"));
        Movie newMovie = new Movie("Один дома",
                "Краткое описание фильма",
                2000,
                null,
                90
        );
        newMovie.setGenres(Set.of(comedyGenre, dramaGenre));
        Movie addedMovie = movieService.add(newMovie);
        CinemaHall addedCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Малый зал",
                        5,
                        10
                )
        );
        CinemaHall secondCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Второй зал",
                        5,
                        10
                )
        );
        Session addedSession = sessionService.add(
                new Session(
                        addedMovie,
                        addedCinemaHall,
                        LocalDateTime.now().plusSeconds(1)
                )
        );
        addedSession.setCinemaHall(secondCinemaHall);
        Assert.assertTrue(sessionService.update(addedSession));
        Session foundSession = sessionService.findById(addedSession.getId());
        Assert.assertEquals(addedSession, foundSession);
        Assert.assertEquals(addedSession.getCinemaHall(), foundSession.getCinemaHall());
    }

    @Test
    public void whenAddTwoThenDeleteOne() {
        Genre comedyGenre = genreService.add(new Genre("Комедия"));
        Genre dramaGenre = genreService.add(new Genre("Драма"));
        Movie firstMovie = new Movie("Один дома",
                "Краткое описание фильма",
                2000,
                null,
                90
        );
        firstMovie.setGenres(Set.of(comedyGenre, dramaGenre));
        Movie addedFirstMovie = movieService.add(firstMovie);
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Малый зал",
                        5,
                        10
                )
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Большой зал",
                        10,
                        10
                )
        );
        Session addedFirstSession = sessionService.add(
                new Session(
                        addedFirstMovie,
                        smallCinemaHall,
                        LocalDateTime.now().plusMinutes(1)
                )
        );
        Movie secondMovie = new Movie("Один дома 2",
                "Краткое описание второго фильма",
                2002,
                null,
                120
        );
        secondMovie.setGenres(Set.of(comedyGenre));
        Movie addedSecondMovie = movieService.add(secondMovie);
        Session addedSecondSession = sessionService.add(
                new Session(
                        addedSecondMovie,
                        largeCinemaHall,
                        LocalDateTime.now().plusMinutes(1)
                )
        );
        Assert.assertTrue(sessionService.delete(addedFirstSession));
        Assert.assertEquals(List.of(addedSecondSession), sessionService.findAll());
    }

    @Test
    public void whenAddTwoThenDeleteBoth() {
        Genre comedyGenre = genreService.add(new Genre("Комедия"));
        Genre dramaGenre = genreService.add(new Genre("Драма"));
        Movie firstMovie = new Movie("Один дома",
                "Краткое описание фильма",
                2000,
                null,
                90
        );
        firstMovie.setGenres(Set.of(comedyGenre, dramaGenre));
        Movie addedFirstMovie = movieService.add(firstMovie);
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Малый зал",
                        5,
                        10
                )
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall(
                        "Большой зал",
                        10,
                        10
                )
        );
        Session addedFirstSession = sessionService.add(
                new Session(
                        addedFirstMovie,
                        smallCinemaHall,
                        LocalDateTime.now().plusMinutes(1)
                )
        );
        Movie secondMovie = new Movie("Один дома 2",
                "Краткое описание второго фильма",
                2002,
                null,
                120
        );
        secondMovie.setGenres(Set.of(comedyGenre));
        Movie addedSecondMovie = movieService.add(secondMovie);
        Session addedSecondSession = sessionService.add(
                new Session(
                        addedSecondMovie,
                        largeCinemaHall,
                        LocalDateTime.now().plusMinutes(1)
                )
        );
        Assert.assertTrue(sessionService.delete(addedFirstSession));
        Assert.assertTrue(sessionService.delete(addedSecondSession));
        Assert.assertEquals(List.of(), sessionService.findAll());
    }

}