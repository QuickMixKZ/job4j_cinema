package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Authority;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User add(User user) {
        checkUser(user);
        return userRepository.add(user);
    }

    public User findById(int id) {
        Optional<User> result = userRepository.findById(id);
        if (result.isEmpty()) {
            throw new NoSuchElementException(String.format("User with ID: %d not found.", id));
        }
        return result.get();
    }

    public User findByUsername(String username) {
        Optional<User> result = userRepository.findByUsername(username);
        if (result.isEmpty()) {
            throw new NoSuchElementException(String.format("User with username \"%s\" not found.", username));
        }
        return result.get();
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean update(User user) {
        checkUser(user);
        return userRepository.update(user);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public boolean delete(User user) {
        User userDb = findById(user.getId());
        return userRepository.delete(user);
    }

    private void checkUser(User user) {
        Optional<User> userDb = userRepository.findByUsernameOrEmailOrPhone(user);
        if (userDb.isPresent()) {
            throw new IllegalArgumentException("User already exists.");
        }
    }

    public Authority findAuthorityByAuthority(String authority) {
        return userRepository.findAuthorityByAuthority(authority);
    }

    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    public List<Authority> findAuthorities() {
        return userRepository.findAuthorities();
    }

    public Authority findAuthorityById(int id) {
        return userRepository.findAuthorityById(id);
    }
}
