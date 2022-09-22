package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.CinemaHall;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CinemaHallRepository {

    private final BasicDataSource pool;
    private static final Logger LOG = LoggerFactory.getLogger(CinemaHallRepository.class.getName());

    public CinemaHallRepository(BasicDataSource pool) {
        this.pool = pool;
    }

    public CinemaHall add(CinemaHall cinemaHall) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO "
                             + "cinema_hall(name, rows_number, seats_per_row)"
                             + "VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, cinemaHall.getName());
            ps.setInt(2, cinemaHall.getRowsNumber());
            ps.setInt(3, cinemaHall.getSeatsPerRow());
            ps.execute();
            ResultSet id = ps.getGeneratedKeys();
            if (id.next()) {
                cinemaHall.setId(id.getInt("id"));
            }
        } catch (SQLException e) {
           LOG.error("Exception in CinemaHallRepository", e);
        }
        return cinemaHall;
    }

    public List<CinemaHall> findAll() {
        List<CinemaHall> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM cinema_hall ORDER BY id")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                result.add(
                        new CinemaHall(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getInt("rows_number"),
                                resultSet.getInt("seats_per_row")
                        )
                );
            }
        } catch (SQLException e) {
            LOG.error("Exception in CinemaHallRepository", e);
        }
        return result;
    }

    public Optional<CinemaHall> findById(int id) {
        Optional<CinemaHall> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM cinema_hall WHERE id = (?)")) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                result = Optional.of(
                        new CinemaHall(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getInt("rows_number"),
                                resultSet.getInt("seats_per_row")
                        )
                );
            }
        } catch (SQLException e) {
            LOG.error("Exception in CinemaHallRepository", e);
        }
        return result;
    }

    public Optional<CinemaHall> findByName(String name) {
        Optional<CinemaHall> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM cinema_hall WHERE name = (?)")) {
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                result = Optional.of(
                        new CinemaHall(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getInt("rows_number"),
                                resultSet.getInt("seats_per_row")
                        )
                );
            }
        } catch (SQLException e) {
            LOG.error("Exception in CinemaHallRepository", e);
        }
        return result;
    }

    public boolean update(CinemaHall cinemaHall) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE cinema_hall "
                             + "SET name = (?), "
                             + "rows_number = (?),"
                             + "seats_per_row = (?)"
                             + "WHERE id = (?)")) {
            ps.setString(1, cinemaHall.getName());
            ps.setInt(2, cinemaHall.getRowsNumber());
            ps.setInt(3, cinemaHall.getSeatsPerRow());
            ps.setInt(4, cinemaHall.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Exception in CinemaHallRepository", e);
        }
        return result;
    }

    public boolean delete(CinemaHall cinemaHall) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM cinema_hall WHERE id = (?)")) {
            ps.setInt(1, cinemaHall.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Exception in CinemaHallRepository", e);
        }
        return result;
    }

    public void deleteAll() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM cinema_hall")) {
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception in CinemaHallRepository", e);
        }
    }
}
