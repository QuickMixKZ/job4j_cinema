package ru.job4j.cinema.model;

import java.time.LocalDate;
import java.util.Objects;

public class Movie {

    private int id;
    private String name;
    private String description;
    private LocalDate premiereDate;

    public Movie() {
    }

    public Movie(int id, String name, String description, LocalDate premiereDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.premiereDate = premiereDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        return id == movie.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Movie{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", premiereDate=" + premiereDate
                + '}';
    }
}
