package ru.job4j.cinema.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.List;

public class UserServiceTest {

    private final UserService userService = new UserService(new UserRepository(new Main().loadPool()));

    @After
    public void wipeTable() {
        userService.deleteAll();
    }

    @Test
    public void whenAddOneThenFindById() {
        User newUser = new User(
                "user",
                "user@users.com",
                "+ 7 (777) 77-77-77",
                "qwerty");
        User addedUser = userService.add(newUser);
        Assert.assertEquals(addedUser, userService.findById(addedUser.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddOneThenFindByWrongId() {
        User newUser = new User(
                "user",
                "user@users.com",
                "+ 7 (777) 77-77-77",
                "qwerty");
        User addedUser = userService.add(newUser);
        userService.findById(0);
    }

    @Test
    public void whenAddTwoThenFindAll() {
        User newUser = new User(
                "user",
                "user@users.com",
                "+ 7 (777) 77-77-77",
                "qwerty");
        User addedUser = userService.add(newUser);
        User newUser1 = new User(
                "user1",
                "user1@users.com",
                "+ 7 (777) 77-77-78",
                "qwerty");
        User addedUser1 = userService.add(newUser1);
        Assert.assertEquals(List.of(addedUser, addedUser1), userService.findAll());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddThenAddWithSameEmail() {
        User newUser = new User(
                "user",
                "user@users.com",
                "+ 7 (777) 77-77-77",
                "qwerty");
        User addedUser = userService.add(newUser);
        User newUser1 = new User(
                "user1",
                "user@users.com",
                "+ 7 (777) 77-77-78",
                "qwerty");
        User addedUser1 = userService.add(newUser1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddThenAddWithSameUsername() {
        User newUser = new User(
                "user",
                "user@users.com",
                "+ 7 (777) 77-77-77",
                "qwerty");
        User addedUser = userService.add(newUser);
        User newUser1 = new User(
                "user",
                "user1@users.com",
                "+ 7 (777) 77-77-78",
                "qwerty");
        User addedUser1 = userService.add(newUser1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddThenAddWithSamePhone() {
        User newUser = new User(
                "user",
                "user@users.com",
                "+ 7 (777) 77-77-77",
                "qwerty");
        User addedUser = userService.add(newUser);
        User newUser1 = new User(
                "user1",
                "user1@users.com",
                "+ 7 (777) 77-77-77",
                "qwerty");
        User addedUser1 = userService.add(newUser1);
    }

    @Test
    public void whenAddThenUpdate() {
        User newUser = new User(
                "user",
                "user@users.com",
                "+ 7 (777) 77-77-77",
                "qwerty");
        User addedUser = userService.add(newUser);
        addedUser.setEmail("new_email@users.com");
        Assert.assertTrue(userService.update(addedUser));
        Assert.assertEquals(addedUser, userService.findById(addedUser.getId()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddThenUpdateToSameUsernameThenException() {
        User newUser = new User(
                "user",
                "user@users.com",
                "+ 7 (777) 77-77-77",
                "qwerty");
        User addedUser = userService.add(newUser);
        User newUser1 = new User(
                "user1",
                "user1@users.com",
                "+ 7 (771) 77-77-77",
                "qwerty");
        User addedUser1 = userService.add(newUser1);
        addedUser.setEmail("user1@users.com");
        userService.update(addedUser);
    }

    @Test
    public void whenAddTwoThenDeleteOne() {
        User newUser = new User(
                "user",
                "user@users.com",
                "+ 7 (777) 77-77-77",
                "qwerty");
        User addedUser = userService.add(newUser);
        User newUser1 = new User(
                "user1",
                "user1@users.com",
                "+ 7 (777) 77-77-78",
                "qwerty");
        User addedUser1 = userService.add(newUser1);
        Assert.assertTrue(userService.delete(addedUser));
        Assert.assertEquals(List.of(addedUser1), userService.findAll());
    }

    @Test
    public void whenAddTwoThenDeleteBoth() {
        User newUser = new User(
                "user",
                "user@users.com",
                "+ 7 (777) 77-77-77",
                "qwerty");
        User addedUser = userService.add(newUser);
        User newUser1 = new User(
                "user1",
                "user1@users.com",
                "+ 7 (777) 77-77-78",
                "qwerty");
        User addedUser1 = userService.add(newUser1);
        Assert.assertTrue(userService.delete(addedUser));
        Assert.assertTrue(userService.delete(addedUser1));
        Assert.assertEquals(List.of(), userService.findAll());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenAddOneThenDeleteWithWrongId() {
        User newUser = new User(
                "user",
                "user@users.com",
                "+ 7 (777) 77-77-77",
                "qwerty");
        User addedUser = userService.add(newUser);
        addedUser.setId(0);
        userService.delete(addedUser);
    }

}