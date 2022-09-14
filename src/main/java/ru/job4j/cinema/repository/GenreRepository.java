package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepository {

    private final BasicDataSource pool;

    public GenreRepository(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Genre> findAll() {
        List<Genre> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM genre")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Genre genre = new Genre(
                  resultSet.getInt("id"),
                  resultSet.getString("name")
                );
                result.add(genre);
            }
        } catch (SQLException e) {

        }
        return result;
    }

    public Genre add(Genre genre) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO genre(name) VALUES(?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, genre.getName());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    genre.setId(id.getInt("id"));
                }
            }
        } catch (SQLException e) {

        }
        return genre;
    }

    public Optional<Genre> findById(int id) {
        Optional<Genre> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM genre WHERE id = (?)")) {
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

        }
        return result;
    }

    public Optional<Genre> findByName(String name) {
        Optional<Genre> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM genre WHERE name = (?)")) {
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
            throw new RuntimeException(e);
        }
        return result;
    }

    public boolean update(Genre genre) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("UPDATE genre SET name = (?) WHERE id = (?)")) {
            ps.setString(1, genre.getName());
            ps.setInt(2, genre.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {

        }
        return result;
    }

    public boolean delete(Genre genre) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM genre WHERE id = (?)")) {
            ps.setInt(1, genre.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {

        }
        return result;
    }

    public void deleteAll() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM genre")) {
            ps.executeUpdate();
        } catch (SQLException e) {

        }
    }
}
