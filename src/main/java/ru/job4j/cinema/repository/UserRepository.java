package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Authority;
import ru.job4j.cinema.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final BasicDataSource pool;

    public UserRepository(BasicDataSource pool) {
        this.pool = pool;
    }

    public User add(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO "
                             + "users(username, email, phone, password, authority_id) "
                             + "VALUES(?, ?, ?, ?, (SELECT id FROM authorities WHERE authority = 'ROLE_USER'))",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.execute();
            ResultSet id = ps.getGeneratedKeys();
            if (id.next()) {
                user.setId(id.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public Optional<User> findById(int id) {
        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement("SELECT * FROM users WHERE id = (?)")) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                User newUser = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"));
                Authority authority = findAuthorityById(resultSet.getInt("authority_id"));
                newUser.setAuthority(authority);
                result = Optional.of(newUser);
            }
        } catch (SQLException e) {

        }
        return result;
    }

    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM users")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                User newUser = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"));
                Authority authority = findAuthorityById(resultSet.getInt("authority_id"));
                newUser.setAuthority(authority);
                result.add(newUser);
            }
        } catch (SQLException e) {

        }
        return result;
    }

    public Optional<User> findByUsernameOrEmailOrPhone(User user) {
        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "SELECT * "
                             + "FROM users "
                             + "WHERE "
                             + "(username = (?) "
                             + "OR email = (?) "
                             + "OR phone = (?)) "
                             + "AND id != (?)")) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setInt(4, user.getId());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                User newUser = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"));
                Authority authority = findAuthorityById(resultSet.getInt("authority_id"));
                newUser.setAuthority(authority);
                result = Optional.of(newUser);
            }
        } catch (SQLException e) {

        }
        return result;
    }

    private Authority findAuthorityById(int id) {
        Authority result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM authorities WHERE id = (?)")) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                result = new Authority(
                                resultSet.getInt("id"),
                                resultSet.getString("authority"));
            }
        } catch (SQLException e) {

        }
        return result;
    }

    public boolean update(User user) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement(
                             "UPDATE users "
                                     + "SET username = (?), "
                                     + "email = (?), "
                                     + "phone = (?), "
                                     + "password = (?) "
                                     + "WHERE id = (?)")) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean delete(User user) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM users WHERE id = (?)")) {
            ps.setInt(1, user.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {

        }
        return result;
    }

    public void deleteAll() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM users")) {
            ps.executeUpdate();
        } catch (SQLException e) {

        }
    }
}
