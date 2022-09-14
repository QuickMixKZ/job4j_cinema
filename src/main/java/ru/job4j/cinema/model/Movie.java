package ru.job4j.cinema.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class Movie {

    private int id;
    private String name;
    private String description;
    private int year;
    private byte[] poster;

    private int duration;
    private Set<Genre> genres;

    public Movie() {
    }

    public Movie(int id, String name, String description, int year, byte[] poster, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.year = year;
        this.poster = poster;
        this.duration = duration;
    }

    public Movie(String name, String description, int year, byte[] poster, int duration) {
        this.name = name;
        this.description = description;
        this.year = year;
        this.poster = poster;
        this.duration = duration;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
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
                + ", year=" + year
                + ", poster=" + Arrays.toString(poster)
                + ", genres=" + genres
                + '}';
    }
}
