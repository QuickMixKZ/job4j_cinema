package ru.job4j.cinema.model;

import java.util.Objects;

public class CinemaHall {

    private int id;
    private String name;
    private int rowsNumber;
    private int seatsPerRow;

    public CinemaHall() {
    }

    public CinemaHall(int id, String name, int rowsNumber, int seatsPerRow) {
        this.id = id;
        this.name = name;
        this.rowsNumber = rowsNumber;
        this.seatsPerRow = seatsPerRow;
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

    public int getRowsNumber() {
        return rowsNumber;
    }

    public void setRowsNumber(int rowsNumber) {
        this.rowsNumber = rowsNumber;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CinemaHall that = (CinemaHall) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CinemaHall{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", rowsNumber=" + rowsNumber
                + ", seatsPerRow=" + seatsPerRow
                + '}';
    }
}
