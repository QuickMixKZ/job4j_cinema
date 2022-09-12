package ru.job4j.cinema.model;

import java.time.LocalDate;
import java.util.Objects;

public class Movie {

    private int id;
    private String name;
    private String description;
    private LocalDate premiereDate;
    private byte[] poster;

    public Movie() {
    }

    public Movie(int id, String name, String description, LocalDate premiereDate, byte[] poster) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.premiereDate = premiereDate;
        this.poster = poster;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPremiereDate() {
        return premiereDate;
    }

    public void setPremiereDate(LocalDate premiereDate) {
        this.premiereDate = premiereDate;
    }

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
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
