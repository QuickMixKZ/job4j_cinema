package ru.job4j.cinema.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Session {

    private int id;
    private Movie movie;
    private CinemaHall cinemaHall;
    private LocalDateTime date;

    public Session() {
    }

    public Session(int id, Movie movie, CinemaHall cinemaHall, LocalDateTime date) {
        this.id = id;
        this.movie = movie;
        this.cinemaHall = cinemaHall;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public CinemaHall getCinemaHall() {
        return cinemaHall;
    }

    public void setCinemaHall(CinemaHall cinemaHall) {
        this.cinemaHall = cinemaHall;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return id == session.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Session{"
                + "id=" + id
                + ", movie=" + movie
                + ", cinemaHall=" + cinemaHall
                + ", date=" + date
                + '}';
    }
}
