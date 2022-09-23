package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository {

    private final BasicDataSource pool;
    private static final Logger LOG = LoggerFactory.getLogger(GenreRepository.class.getName());
    private static final String SELECT_QUERY = "SELECT * FROM genre";
    private static final String INSERT_QUERY = "INSERT INTO genre(name) VALUES(?)";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM genre WHERE id = (?)";
    private static final String SELECT_BY_NAME_QUERY = "SELECT * FROM genre WHERE name = (?)";
    private static final String UPDATE_QUERY = "UPDATE genre SET name = (?) WHERE id = (?)";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM genre WHERE id = (?)";
    private static final String DELETE_ALL_QUERY = "DELETE FROM genre";

    public GenreRepository(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Genre> findAll() {
        List<Genre> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(SELECT_QUERY)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Genre genre = new Genre(
                  resultSet.getInt("id"),
                  resultSet.getString("name")
                );
                result.add(genre);
            }
        } catch (SQLException e) {
            LOG.error("Exception in GenreRepository", e);
        }
        return result;
    }

    public Genre add(Genre genre) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT_QUERY,
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, genre.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    genre.setId(id.getInt("id"));
                }
            }
        } catch (SQLException e) {
            LOG.error("Exception in GenreRepository", e);
        }
        return genre;
    }

    public Optional<Genre> findById(int id) {
        Optional<Genre> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID_QUERY)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                result = Optional.of(
                        new Genre(
                                resultSet.getInt("id"),
                                resultSet.getString("name")
                        )
                );
            }
        } catch (SQLException e) {
            LOG.error("Exception in GenreRepository", e);
        }
        return result;
    }

    public Optional<Genre> findByName(String name) {
        Optional<Genre> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_NAME_QUERY)) {
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                result = Optional.of(
                        new Genre(
                                resultSet.getInt("id"),
                                resultSet.getString("name")
                        )
                );
            }
        } catch (SQLException e) {
            LOG.error("Exception in GenreRepository", e);
        }
        return result;
    }

    public boolean update(Genre genre) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(UPDATE_QUERY)) {
            ps.setString(1, genre.getName());
            ps.setInt(2, genre.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Exception in GenreRepository", e);
        }
        return result;
    }

    public boolean delete(Genre genre) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(DELETE_BY_ID_QUERY)) {
            ps.setInt(1, genre.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("Exception in GenreRepository", e);
        }
        return result;
    }

    public void deleteAll() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(DELETE_ALL_QUERY)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Exception in GenreRepository", e);
        }
    }
}
