package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TicketRepository {

    private final BasicDataSource pool;
    private static final Logger LOG = LoggerFactory.getLogger(TicketRepository.class.getName());

    public TicketRepository(BasicDataSource pool) {
        this.pool = pool;
    }

    public boolean createTickets(Ticket[] tickets) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO "
                             + "ticket(session_id, user_id, pos_row, seat) "
                             + "VALUES(?, ?, ? ,?)")) {
            cn.setAutoCommit(false);
            for (Ticket ticket : tickets) {
                ps.setInt(1, ticket.getSession().getId());
                ps.setInt(2, ticket.getUser().getId());
                ps.setInt(3, ticket.getRow());
                ps.setInt(4, ticket.getSeat());
                ps.addBatch();
            }
            ps.executeBatch();
            cn.commit();
            result = true;
        } catch (SQLException e) {
            LOG.error("Exception in TicketRepository", e);
        }
        return result;
    }

    public List<Ticket> findActualTicketsByUser(User user) {
        List<Ticket> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(
                    "select "
                            + "ticket.id AS ticket_id, "
                            + "ticket.pos_row AS ticket_pos_row, "
                            + "ticket.seat AS ticket_seat, "
                            + "sessions.start_date AS sessions_start_date, "
                            + "movie.name AS movie_name, "
                            + "movie.duration AS movie_duration, "
                            + "cinema_hall.name AS cinema_hall_name "
                            + "from "
                            + "ticket "
                            + "JOIN "
                            + "sessions ON ticket.session_id = sessions.id "
                            + "JOIN "
                            + "movie ON sessions.movie_id = movie.id "
                            + "JOIN "
                            + "cinema_hall ON sessions.cinema_hall_id = cinema_hall.id "
                            + "WHERE ticket.user_id = (?) "
                            + "AND sessions.start_date > now()"
            )) {
            ps.setInt(1, user.getId());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Movie movie = new Movie(
                        resultSet.getString("movie_name"),
                        resultSet.getInt("movie_duration")
                );
                CinemaHall cinemaHall = new CinemaHall(
                        resultSet.getString("cinema_hall_name")
                );
                Session session = new Session(
                        movie,
                        cinemaHall,
                        resultSet.getTimestamp("sessions_start_date").toLocalDateTime()
                );
                result.add(
                        new Ticket(
                                resultSet.getInt("ticket_id"),
                                session,
                                user,
                                resultSet.getInt("ticket_pos_row"),
                                resultSet.getInt("ticket_seat")

                        )
                );
            }
        } catch (SQLException e) {
            LOG.error("Exception in TicketRepository", e);
        }
        return result;
    }
}