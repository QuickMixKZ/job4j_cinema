package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.model.Movie;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.*;

@Repository
public class MovieRepository {

    private final BasicDataSource pool;

    public MovieRepository(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Movie> findAll() {
        List<Movie> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM movie")) {
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
                movie.setGenres(findGenresByMovie(movie));
                result.add(movie);
            }
        } catch (SQLException e) {

        }
        return result;
    }

    public Optional<Movie> findById(int id) {
        Optional<Movie> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM movie WHERE id = (?)")) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                Movie movie = new Movie(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("year"),
                        resultSet.getBytes("poster"),
                        resultSet.getInt("duration")
                );
                movie.setGenres(findGenresByMovie(movie));
                result = Optional.of(movie);
            }
        } catch (SQLException e) {

        }
        return result;
    }

    public Optional<Movie> findByNameAndYear(String name, int year) {
        Optional<Movie> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM movie WHERE name = (?) AND year = (?)")) {
            ps.setString(1, name);
            ps.setInt(2, year);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                Movie movie = new Movie(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("year"),
                        resultSet.getBytes("poster"),
                        resultSet.getInt("duration")
                );
                movie.setGenres(findGenresByMovie(movie));
                result = Optional.of(movie);
            }
        } catch (SQLException e) {

        }
        return result;
    }

    public Movie add(Movie movie) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO movie(name, description, year, poster, duration) VALUES (?, ?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, movie.getName());
            ps.setString(2, movie.getDescription());
            ps.setInt(3, movie.getYear());
            ps.setBinaryStream(4, movie.getPoster() == null ? null : new ByteArrayInputStream(movie.getPoster()));
            ps.setInt(5, movie.getDuration());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    movie.setId(id.getInt("id"));
                }
            }
            cn.setAutoCommit(false);
            PreparedStatement genrePS = cn.prepareStatement("INSERT INTO movie_genre(movie_id, genre_id) VALUES(?, ?)");
            for (Genre genre : movie.getGenres()) {
                genrePS.setInt(1, movie.getId());
                genrePS.setInt(2, genre.getId());
                genrePS.addBatch();
            }
            ps.executeBatch();
            cn.commit();
        } catch (SQLException e) {

        }
        return movie;
    }

    private Set<Genre> findGenresByMovie(Movie movie) {
        Set<Genre> result = new HashSet<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT "
                             + "g.id AS id, "
                             + "g.name AS name "
                             + "FROM movie_genre AS mg "
                             + "JOIN genre AS g ON mg.genre_id = g.id "
                             + "WHERE mg.movie_id = (?)")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                result.add(
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

    public boolean delete(Movie movie) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM movie WHERE id = (?)")) {
            ps.setInt(1, movie.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {

        }
        return result;
    }

    public boolean update(Movie movie) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement(
                             "UPDATE movie "
                                     + "SET name = (?), "
                                     + "description = (?), "
                                     + "year = (?), "
                                     + "poster = (?),"
                                     + "duration = (?)"
                                     + "WHERE id = (?)")) {
            ps.setString(1, movie.getName());
            ps.setString(2, movie.getDescription());
            ps.setInt(3, movie.getYear());
            ps.setBinaryStream(4,
                    movie.getPoster() == null ? null : new ByteArrayInputStream(movie.getPoster()));
            ps.setInt(5, movie.getDuration());
            ps.setInt(6, movie.getId());
            result = ps.executeUpdate() > 0;
            updateGenresInMovie(movie);
        } catch (SQLException e) {

        }
        return result;
    }

    private void updateGenresInMovie(Movie movie) {
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement("DELETE FROM movie_genre WHERE movie_id = (?)")) {
            ps.setInt(1, movie.getId());
            ps.executeUpdate();
            cn.setAutoCommit(false);
            PreparedStatement genrePS = cn.prepareStatement("INSERT INTO movie_genre(movie_id, genre_id) VALUES(?, ?)");
            for (Genre genre : movie.getGenres()) {
                genrePS.setInt(1, movie.getId());
                genrePS.setInt(2, genre.getId());
                genrePS.addBatch();
            }
            ps.executeBatch();
            cn.commit();
        } catch (SQLException e) {

        }
    }

    public void deleteAll() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM movie")) {
            ps.executeUpdate();
        } catch (SQLException e) {

        }
    }
}
