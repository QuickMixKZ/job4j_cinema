package ru.job4j.cinema.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.service.UserService;
import ru.job4j.cinema.util.ControllerUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {

    private final UserService userService;
    private final TicketService ticketService;
    private final PasswordEncoder encoder;
    private final ControllerUtil controllerUtil;

    public UserController(UserService userService,
                          TicketService ticketService,
                          PasswordEncoder encoder,
                          ControllerUtil controllerUtil) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.encoder = encoder;
        this.controllerUtil = controllerUtil;
    }

    @PostMapping("/reg")
    public String regSave(@ModelAttribute User user) {
        user.setEnabled(true);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAuthority(userService.findAuthorityByAuthority("ROLE_USER"));
        if (userService.existsByName(user.getUsername())) {
            return "redirect:/reg?error=true";
        }
        userService.add(user);
        return "redirect:/login";
    }

    @GetMapping("/profile/tickets")
    public String getUsersActualTicket(Model model) {
        controllerUtil.addUserToModel(model);
        User currentUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("tickets", ticketService.findActualTicketsByUser(currentUser));
        return "usersTickets";
    }

    @GetMapping("/reg")
    public String regPage(Model model,
                          @RequestParam(value = "error", required = false) String error) {
        String errorMessage = null;
        if (error != null) {
            errorMessage = "User with the given name already exists!";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "registration";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Username or Password is incorrect !!";
        }
        if (logout != null) {
            errorMessage = "You have been successfully logged out !!";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout=true";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        controllerUtil.addUserToModel(model);
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(Model model,
                           @PathVariable(name = "id") int id) {
        controllerUtil.addUserToModel(model);
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("authorities", userService.findAuthorities());
        return "editUser";
    }

    @PostMapping("/users/editUser")
    public String editUser(@ModelAttribute(name = "user") User user) {
        user.setAuthority(userService.findAuthorityById(user.getAuthority().getId()));
        userService.update(user);
        return "redirect:/users";
    }
}
