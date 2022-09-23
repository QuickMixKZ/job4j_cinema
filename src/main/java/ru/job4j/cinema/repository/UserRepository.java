package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(UserRepository.class.getName());

    public UserRepository(BasicDataSource pool) {
        this.pool = pool;
    }

    private static final String INSERT_QUERY = "INSERT INTO "
            + "users(username, email, phone, password, authority_id) "
            + "VALUES(?, ?, ?, ?, (SELECT id FROM authorities WHERE authority = 'ROLE_USER'))";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM users WHERE id = (?)";
    private static final String SELECT_BY_USERNAME_QUERY = "SELECT * FROM users WHERE username = (?)";
    private static final String SELECT_QUERY = "SELECT * FROM users";
    private static final String SELECT_BY_UNIQUE_FIELDS_QUERY = "SELECT * "
            + "FROM users "
            + "WHERE "
            + "(username = (?) "
            + "OR email = (?) "
            + "OR phone = (?)) "
            + "AND id != (?)";
    private static final String SELECT_AUTHORITY_BY_ID_QUERY = "SELECT * FROM authorities WHERE id = (?)";
    private static final String SELECT_AUTHORITY_BY_AUTHORITY_QUERY = "SELECT * FROM authorities WHERE authority = (?)";
    private static final String SELECT_AUTHORITY_QUERY = "SELECT * FROM authorities";
    private static final String UPDATE_QUERY = "UPDATE users "
            + "SET username = (?), "
            + "email = (?), "
            + "phone = (?), "
            + "password = (?), "
            + "enabled = (?), "
            + "authority_id = (?)"
            + "WHERE id = (?)";
    private static final String SELECT_ID_BY_USERNAME_QUERY = "SELECT id FROM users WHERE username = (?)";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = (?)";
    private static final String DELETE_ALL_QUERY = "DELETE FROM users";

    public User add(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     INSERT_QUERY,
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
            LOG.error("UserRepository in TicketRepository", e);
        }
        return user;
    }

    public Optional<User> findById(int id) {
        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID_QUERY)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                User newUser = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("enabled"));
                Authority authority = findAuthorityById(resultSet.getInt("authority_id"));
                newUser.setAuthority(authority);
                result = Optional.of(newUser);
            }
        } catch (SQLException e) {
            LOG.error("UserRepository in TicketRepository", e);
        }
        return result;
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_USERNAME_QUERY)) {
            ps.setString(1, username);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                User newUser = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("enabled"));
                Authority authority = findAuthorityById(resultSet.getInt("authority_id"));
                newUser.setAuthority(authority);
                result = Optional.of(newUser);
            }
        } catch (SQLException e) {
            LOG.error("UserRepository in TicketRepository", e);
        }
        return result;
    }

    public List<User> findAll() {
        List<User> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_QUERY)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                User newUser = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("enabled"));
                Authority authority = findAuthorityById(resultSet.getInt("authority_id"));
                newUser.setAuthority(authority);
                result.add(newUser);
            }
        } catch (SQLException e) {
            LOG.error("UserRepository in TicketRepository", e);
        }
        return result;
    }

    public Optional<User> findByUsernameOrEmailOrPhone(User user) {
        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_UNIQUE_FIELDS_QUERY)) {
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
                        resultSet.getString("password"),
                        resultSet.getBoolean("enabled"));
                Authority authority = findAuthorityById(resultSet.getInt("authority_id"));
                newUser.setAuthority(authority);
                result = Optional.of(newUser);
            }
        } catch (SQLException e) {
            LOG.error("UserRepository in TicketRepository", e);
        }
        return result;
    }

    public Authority findAuthorityById(int id) {
        Authority result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_AUTHORITY_BY_ID_QUERY)) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                result = new Authority(
                                resultSet.getInt("id"),
                                resultSet.getString("authority"));
            }
        } catch (SQLException e) {
            LOG.error("UserRepository in TicketRepository", e);
        }
        return result;
    }

    public Authority findAuthorityByAuthority(String authority) {
        Authority result = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_AUTHORITY_BY_AUTHORITY_QUERY)) {
            ps.setString(1, authority);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                result = new Authority(
                        resultSet.getInt("id"),
                        resultSet.getString("authority"));
            }
        } catch (SQLException e) {
            LOG.error("UserRepository in TicketRepository", e);
        }
        return result;
    }

    public List<Authority> findAuthorities() {
        List<Authority> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_AUTHORITY_QUERY)) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                result.add(new Authority(
                        resultSet.getInt("id"),
                        resultSet.getString("authority")
                        )
                );
            }
        } catch (SQLException e) {
            LOG.error("UserRepository in TicketRepository", e);
        }
        return result;
    }

    public boolean update(User user) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement(UPDATE_QUERY)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPassword());
            ps.setBoolean(5, user.isEnabled());
            ps.setInt(6, user.getAuthority().getId());
            ps.setInt(7, user.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("UserRepository in TicketRepository", e);
        }
        return result;
    }

    public boolean existsByName(String name) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
        PreparedStatement ps = cn.prepareStatement(SELECT_ID_BY_USERNAME_QUERY)) {
            ps.setString(1, name);
            ResultSet resultSet = ps.executeQuery();
            result = resultSet.next();
        } catch (SQLException e) {
            LOG.error("UserRepository in TicketRepository", e);
        }
        return result;
    }

    public boolean delete(User user) {
        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(DELETE_QUERY)) {
            ps.setInt(1, user.getId());
            result = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.error("UserRepository in TicketRepository", e);
        }
        return result;
    }

    public void deleteAll() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(DELETE_ALL_QUERY)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.error("UserRepository in TicketRepository", e);
        }
    }
}
