package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.List;
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
            throw new IllegalArgumentException(String.format("User with ID: %d not found.", id));
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
}
