package ru.job4j.cinema.model;

import java.util.Objects;

public class Ticket {

    private int id;
    private Session session;
    private User user;
    private int row;
    private int seat;

    public Ticket() {
    }

    public Ticket(int id, Session session, User user, int row, int seat) {
        this.id = id;
        this.session = session;
        this.user = user;
        this.row = row;
        this.seat = seat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return id == ticket.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ticket{"
                + "id=" + id
                + ", session=" + session
                + ", user=" + user
                + ", row=" + row
                + ", seat=" + seat
                + '}';
    }
}
