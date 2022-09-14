package ru.job4j.cinema.service;

import org.junit.After;
import org.junit.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.CinemaHall;
import ru.job4j.cinema.repository.CinemaHallRepository;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CinemaHallServiceTest {

    private final CinemaHallService cinemaHallService =
            new CinemaHallService(new CinemaHallRepository(new Main().loadPool()));

    @After
    public void wipeTable() {
        cinemaHallService.deleteAll();
    }

    @Test
    public void whenAddTwoThenFindAll() {
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 5, 10)
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall("Большой зал", 10, 10)
        );
        assertEquals(List.of(smallCinemaHall, largeCinemaHall), cinemaHallService.findAll());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddTwoWithSameNameThenException() {
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 5, 10)
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 10, 10)
        );
    }

    @Test
    public void whenAddTwoThenFindById() {
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 5, 10)
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall("Большой зал", 10, 10)
        );
        assertEquals(smallCinemaHall, cinemaHallService.findById(smallCinemaHall.getId()));
        assertEquals(largeCinemaHall, cinemaHallService.findById(largeCinemaHall.getId()));
    }

    @Test
    public void whenAddTwoThenFindByName() {
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 5, 10)
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall("Большой зал", 10, 10)
        );
        assertEquals(smallCinemaHall, cinemaHallService.findByName(smallCinemaHall.getName()));
        assertEquals(largeCinemaHall, cinemaHallService.findByName(largeCinemaHall.getName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddTwoThenFindByWrongId() {
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 5, 10)
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall("Большой зал", 10, 10)
        );
        cinemaHallService.findById(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddTwoThenFindByWrongName() {
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 5, 10)
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall("Большой зал", 10, 10)
        );
        cinemaHallService.findByName("Средний зал");
    }

    @Test
    public void whenAddTwoThenUpdateOneAndFindByNewName() {
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 5, 10)
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall("Большой зал", 10, 10)
        );
        smallCinemaHall.setName("Малый зал №1");
        assertTrue(cinemaHallService.update(smallCinemaHall));
        assertEquals(smallCinemaHall, cinemaHallService.findByName(smallCinemaHall.getName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddTwoThenUpdateOneWithWrongID() {
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 5, 10)
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall("Большой зал", 10, 10)
        );
        smallCinemaHall.setName("Малый зал №1");
        smallCinemaHall.setId(0);
        cinemaHallService.update(smallCinemaHall);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddTwoThenUpdateOneToSameNameThenException() {
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 5, 10)
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall("Большой зал", 10, 10)
        );
        smallCinemaHall.setName("Большой зал");
        cinemaHallService.update(smallCinemaHall);
    }

    @Test
    public void whenAddTwoThenDeleteOneTheFindAll() {
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 5, 10)
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall("Большой зал", 10, 10)
        );
        assertTrue(cinemaHallService.delete(smallCinemaHall));
        assertEquals(List.of(largeCinemaHall), cinemaHallService.findAll());
    }

    @Test
    public void whenAddTwoThenDeleteBothTheFindAll() {
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 5, 10)
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall("Большой зал", 10, 10)
        );
        assertTrue(cinemaHallService.delete(smallCinemaHall));
        assertTrue(cinemaHallService.delete(largeCinemaHall));
        assertEquals(List.of(), cinemaHallService.findAll());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddTwoThenDeleteAlreadyDeletedThenException() {
        CinemaHall smallCinemaHall = cinemaHallService.add(
                new CinemaHall("Малый зал", 5, 10)
        );
        CinemaHall largeCinemaHall = cinemaHallService.add(
                new CinemaHall("Большой зал", 10, 10)
        );
        cinemaHallService.delete(smallCinemaHall);
        cinemaHallService.delete(smallCinemaHall);

    }

}