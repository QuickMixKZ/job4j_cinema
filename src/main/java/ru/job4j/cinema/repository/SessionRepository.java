package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.CinemaHall;
import ru.job4j.cinema.model.Movie;
import ru.job4j.cinema.model.Session;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SessionRepository {

    private final BasicDataSource pool;
    private final MovieRepository movieRepository;
    private static final Logger LOG = LoggerFactory.getLogger(SessionRepository.class.getName());
    private static final String SELECT_QUERY = "SELECT "
            + "movie.id AS movie_id, "
            + "movie.name AS movie_name, "
            + "movie.description AS movie_description, "
            + "movie.duration AS movie_duration, "
            + "movie.poster AS movie_poster, "
            + "movie.year AS movie_year, "
            + "sessions.id AS sessions_id, "
            + "sessions.movie_id AS sessions_movie_id, "
            + "sessions.cinema_hall_id AS sessions_cinema_hall_id, "
            + "sessions.start_date AS sessions_start_date, "
            + "cinema_hall.id AS cinema_hall_id, "
            + "cinema_hall.name AS cinema_hall_name, "
            + "cinema_hall.rows_number AS cinema_hall_rows_number, "
            + "cinema_hall.seats_per_row AS cinema_hall_seats_per_row "
            + "FROM "
            + "sessions "
            + "JOIN movie ON sessions.movie_id = movie.id "
            + "JOIN cinema_hall ON sessions.cinema_hall_id = cinema_hall.id "
            + "ORDER BY sessions.id";
    private static final String INSERT_QUERY = "INSERT INTO sessions(movie_id, cinema_hall_id, start_date) VALUES (?, ?, ?)";
    private static final String SELECT_BY_ID_QUERY = "SELECT "
            + "movie.id AS movie_id, "
            + "movie.name AS movie_name, "
            + "movie.description AS movie_description, "
            + "movie.duration AS movie_duration, "
            + "movie.poster AS movie_poster, "
            + "movie.year AS movie_year, "
            + "sessions.id AS sessions_id, "
            + "sessions.movie_id AS sessions_movie_id, "
            + "sessions.cinema_hall_id AS sessions_cinema_hall_id, "
            + "sessions.start_date AS sessions_start_date, "
            + "cinema_hall.id AS cinema_hall_id, "
            + "cinema_hall.name AS cinema_hall_name, "
            + "cinema_hall.rows_number AS cinema_hall_rows_number, "
            + "cinema_hall.seats_per_row AS cinema_hall_seats_per_row "
            + "FROM "
            + "sessions "
            + "JOIN movie ON sessions.movie_id = movie.id "
            + "JOIN cinema_hall ON sessions.cinema_hall_id = cinema_hall.id "
            + "WHERE sessions.id = (?)";
    private static final String UPDATE_QUERY = "UPDATE sessions "
            + "SET movie_id = (?), "
            + "cinema_hall_id = (?), "
            + "start_date = (?) "
            + "WHERE id = (?)";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM sessions WHERE id = (?)";
    private static final String SELECT_TODAY_MOVIES_QUERY = "SELECT DISTINCT "
            + "movie.* "
            + "FROM "
            + "sessions "
            + "JOIN movie ON sessions.movie_id = movie.id "
            + "WHERE sessions.start_date > now() "
            + "AND sessions.start_date < (date_trunc('day', now()) + interval '30 hour')";
    private static final String SELECT_BY_MOVIE_ID_QUERY = "SELECT "
            + "movie.id AS movie_id, "
            + "movie.name AS movie_name, "
            + "movie.description AS movie_description, "
            + "movie.duration AS movie_duration, "
            + "movie.poster AS movie_poster, "
            + "movie.year AS movie_year, "
            + "sessions.id AS sessions_id, "
            + "sessions.movie_id AS sessions_movie_id, "
            + "sessions.cinema_hall_id AS cinema_hall_id, "
            + "sessions.start_date AS sessions_start_date, "
            + "cinema_hall.name AS cinema_hall_name, "
            + "cinema_hall.rows_number AS cinema_hall_rows_number, "
            + "cinema_hall.seats_per_row AS cinema_hall_seats_per_row "
            + "FROM "
            + "sessions "
            + "JOIN movie ON sessions.movie_id = movie.id "
            + "JOIN cinema_hall ON sessions.cinema_hall_id = cinema_hall.id "
            + "WHERE sessions.movie_id = (?)";
    private static final String SELECT_TODAY_MOVIE_BY_ID_QUERY = "SELECT "
            + "movie.id AS movie_id, "
            + "movie.name AS movie_name, "
            + "movie.description AS movie_description, "
            + "movie.duration AS movie_duration, "
            + "movie.poster AS movie_poster, "
            + "movie.year AS movie_year, "
            + "sessions.id AS sessions_id, "
            + "sessions.movie_id AS sessions_movie_id, "
            + "sessions.cinema_hall_id AS cinema_hall_id, "
            + "sessions.start_date AS sessions_start_date, "
            + "cinema_hall.name AS cinema_hall_name, "
            + "cinema_hall.rows_number AS cinema_hall_rows_number, "
            + "cinema_hall.seats_per_row AS cinema_hall_seats_per_row "
            + "FROM "
            + "sessions "
            + "JOIN movie ON sessions.movie_id = movie.id "
            + "JOIN cinema_hall ON sessions.cinema_hall_id = cinema_hall.id "
            + "WHERE sessions.movie_id = (?) "
            + "AND sessions.start_date > now() "
            + "AND sessions.start_date < (date_trunc('day', now()) + interval '30 hour')";
    private static final String SELECT_CINEMAHALL_BY_SESSION_ID_QUERY = "SELECT "
            + "cinema_hall.rows_number, "
            + "cinema_hall.seats_per_row "
            + "FROM sessions "
            + "JOIN cinema_hall ON sessions.cinema_hall_id = cinema_hall.id "
            + "WHERE sessions.id = (?)";
    private static final String SELECT_TICKETS_BY_SESSION_ID_QUERY = "SELECT "
            + "ticket.pos_row, "
            + "ticket.seat "
            + "FROM sessions "
            + "JOIN ticket ON sessions.id = ticket.session_id "
            + "WHERE sessions.id = (?)";
    private static final String DELETE_ALL_QUERY = "DELETE FROM sessions";

    public SessionRepository(BasicDataSource pool, MovieRepository movieRepository) {
        this.pool = pool;
        this.movieRepository = movieRepository;
    }

    public Session add(Session session) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, session.getMovie().getId());
            ps.setInt(2, session.getCinemaHall().getId());
            ps.setTimestamp(3, Timestamp.valueOf(session.getDate()));
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    session.setId(id.getInt("id"));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in SessionRepository", e);
        }
        return session;
    }

    public List<Session> findAll() {
        List<Session> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_QUERY)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                result.add(createSessionFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Exception in SessionRepository", e);
        }
        return result;
    }

    public Optional<Session> findById(int id) {
        Optional<Session> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     SELECT_BY_ID_QUERY)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                result = Optional.of(createSessionFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Exception in SessionRepository", e);
        }
        return result;
    }

    public boolean update(Session session) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement(UPDATE_QUERY)) {
            ps.setInt(1, session.getMovie().getId());
            ps.setInt(2, session.getCinemaHall().getId());
            ps.setTimestamp(3, Timestamp.valueOf(session.getDate()));
            ps.setInt(4, session.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Exception in SessionRepository", e);
        }
        return result;
    }

    public boolean delete(Session session) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(DELETE_BY_ID_QUERY)) {
            ps.setInt(1, session.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Exception in SessionRepository", e);
        }
        return result;
    }

    public List<Movie> findTodayMovies() {
        List<Movie> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     SELECT_TODAY_MOVIES_QUERY)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Movie movie = new Movie(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("year"),
                        resultSet.getBytes("poster"),
                        resultSet.getInt("duration")
                );
                movie.setGenres(movieRepository.findGenresByMovie(movie));
                result.add(movie);
            }
        } catch (SQLException e) {
            LOG.error("Exception in SessionRepository", e);
        }
        return result;
    }

    public List<Session> findByMovieId(int movieId) {
        List<Session> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     SELECT_BY_MOVIE_ID_QUERY)) {
            ps.setInt(1, movieId);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                result.add(createSessionFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Exception in SessionRepository", e);
        }
        return result;
    }

    public List<Session> findTodayByMovieId(int movieId) {
        List<Session> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     SELECT_TODAY_MOVIE_BY_ID_QUERY)) {
            ps.setInt(1, movieId);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                result.add(createSessionFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            LOG.error("Exception in SessionRepository", e);
        }
        return result;
    }

    public boolean[][] findSeatsBySessionId(int id) {
        boolean[][] result = new boolean[0][0];
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(
                SELECT_CINEMAHALL_BY_SESSION_ID_QUERY)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                int rows = resultSet.getInt("rows_number");
                int seatsPerRow = resultSet.getInt("seats_per_row");
                result = new boolean[rows][seatsPerRow];
            }
            PreparedStatement ticketsPs = cn.prepareStatement(
                    SELECT_TICKETS_BY_SESSION_ID_QUERY);
            ticketsPs.setInt(1, id);
            ResultSet ticketsRs = ticketsPs.executeQuery();
            while (ticketsRs.next()) {
                int posRow = ticketsRs.getInt("pos_row");
                int seat = ticketsRs.getInt("seat");
                result[posRow - 1][seat - 1] = true;
            }
        } catch (SQLException e) {
            LOG.error("Exception in SessionRepository", e);
        }
        return result;
    }

    public void deleteAll() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(DELETE_ALL_QUERY)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception in SessionRepository", e);
        }
    }

    private Session createSessionFromResultSet(ResultSet resultSet) throws SQLException {
        Movie movie = new Movie(
                resultSet.getInt("movie_id"),
                resultSet.getString("movie_name"),
                resultSet.getString("movie_description"),
                resultSet.getInt("movie_year"),
                resultSet.getBytes("movie_poster"),
                resultSet.getInt("movie_duration")
        );
        movie.setGenres(movieRepository.findGenresByMovie(movie));
        CinemaHall cinemaHall = new CinemaHall(
                resultSet.getInt("cinema_hall_id"),
                resultSet.getString("cinema_hall_name"),
                resultSet.getInt("cinema_hall_rows_number"),
                resultSet.getInt("cinema_hall_seats_per_row")
        );
        return new Session(
                resultSet.getInt("sessions_id"),
                movie,
                cinemaHall,
                resultSet.getTimestamp("sessions_start_date").toLocalDateTime()
        );
    }

}
